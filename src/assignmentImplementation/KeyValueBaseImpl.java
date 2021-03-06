package assignmentImplementation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.KeyValueBase;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Predicate;

public class KeyValueBaseImpl implements KeyValueBase<KeyImpl, ValueListImpl> {

	private static final String filePath = "kvbstore";
	private static final long memorySize = 1024 * 1024 * 1024L;

	/**
	 * Index manages the data storage layers
	 */
	private static IndexImpl index = new IndexImpl(filePath, memorySize);

	/**
	 * We need to track state - mainly to prevent access during initialisation
	 * 
	 * @author jens
	 * 
	 */
	private enum State {
		UNINITIALISED, INITIALISING, READY
	}

	private static State currentState = State.UNINITIALISED;
	// Use a lock to protect the service during initialisation
	protected static ReentrantLock initializationLock = new ReentrantLock();

	/**
	 * Constructor: creates index with specified size and file location
	 */
	public KeyValueBaseImpl() {
	}

	/**
	 * This function gets as input a path name to a file (stored on the machine
	 * running the code) containing partial key-value mappings and initialises
	 * the key-value store. Assumes that the input file keys are grouped by key!
	 * Otherwise it will raise errors
	 * 
	 * It needs to check if the file exists; check that the service is not
	 * already active Then parse the input file into the key-value store
	 */
	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {

		// lock the initialization
		initializationLock.lock();
		displayMessage("Initialising KVB");
		try {

			// State-based error handling
			// If the object is already initialised, then throw an exception
			if (currentState == State.READY)
				throw new ServiceAlreadyInitializedException();

			// If a new request comes during initialisation, we need to raise an
			// exception. This should not be reached thanks to the lock
			else if (currentState == State.INITIALISING)
				throw new ServiceInitializingException();

			else {
				// set state to initialising immediately
				currentState = State.INITIALISING;

				try {
					BufferedReader reader = new BufferedReader(new FileReader(
							serverFilename));
					String line = null;
					String whitespace = "[ \t]+";

					// handle first line:
					if ((line = reader.readLine()) != null) {
						String[] split = line.split(whitespace);
						if (split.length > 2) {
							// TODO: either throw exception or skip this line
							throw new ServiceInitializingException(
									"Badly formatted file - each line should contain a key (integer) and a value (integer)");
						}
						KeyImpl key = parseKey(split[0]);
						ValueImpl value = parseValue(split[1]);
						ValueListImpl vl = new ValueListImpl();
						vl.add(value);
						// loop over remaining lines
						while ((line = reader.readLine()) != null) {

							split = line.split(whitespace);
							KeyImpl nextKey = parseKey(split[0]);
							ValueImpl nextValue = parseValue(split[1]);

							if (nextKey.compareTo(key) == 0) {
								// Same key as before, append the value
								vl.add(nextValue);
							} else {
								// insert the last key and start the next one
								index.insert(key, vl);
								key = nextKey;
								vl = new ValueListImpl();
								vl.add(nextValue);

								// if end of file, won't import this value
							}

						}
						// final value insertion
						index.insert(key, vl);
					}

					reader.close();

					displayMessage("Finished parsing input file "
							+ serverFilename);
					currentState = State.READY;

				} catch (FileNotFoundException ex) {
					currentState = State.UNINITIALISED;
					throw new FileNotFoundException(); // pass along to caller
				} catch (IOException x) {
					currentState = State.UNINITIALISED;
					System.err.format("IOException: %s%n", x);
					throw new ServiceInitializingException();
				} catch (KeyAlreadyPresentException e) {
					currentState = State.UNINITIALISED;
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// any clean up handled here - whether success or failure
					displayMessage("finished initialisation");
				}
			}
		} finally {
			initializationLock.unlock();
		}

	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		else {
			return index.get(k);
		}

	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		else {
			index.insert(k, v);
		}

	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		else {
			index.update(k, newV);
		}

	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		else {
			index.remove(k);
		}
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();

		else if (end.compareTo(begin) == -1) {
			throw new BeginGreaterThanEndException(begin, end);
		} else {
			List<ValueListImpl> outlist = new ArrayList<ValueListImpl>();
			List<ValueListImpl> list = index.scan(begin, end);
			if (!list.isEmpty()) {
				for (ValueListImpl vl : list) {
					if (p.evaluate(vl))
						outlist.add(vl);
				}
			}
			return outlist;
		}

	}

	/**
	 * Extensions - may be needed for passing assignment?
	 */

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {

		if (currentState != State.READY)
			throw new ServiceNotInitializedException();

		// atomically retrieve all the keys - may take some time if contention
		// issues
		List<ValueListImpl> all = index.atomicScan(begin, end);
		List<ValueListImpl> results = new ArrayList<ValueListImpl>();

		// evaluate all values
		for (ValueListImpl v : all) {
			if (p.evaluate(v))
				results.add(v);
		}
		return results;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();

		// call bulkput on index
		index.bulkPut(mappings);

	}

	// /////////////////////////////////////////////////////////////////////////////////
	// UTILITY METHODS

	// Parse a string to a key
	private KeyImpl parseKey(String s) throws NumberFormatException {
		int id = Integer.parseInt(s);
		return new KeyImpl(new Integer(id));
	}

	// Parse a string to value
	private ValueImpl parseValue(String s) throws NumberFormatException {
		int value = Integer.parseInt(s);
		return new ValueImpl(value);
	}

	// Prints a message with the current ThreadID
	protected void displayMessage(String message) {
		System.out.println("KeyValueBaseImpl: " + message + " ~  "
				+ Thread.currentThread());
	}

}
