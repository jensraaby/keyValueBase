package assignmentImplementation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Index manages the data storage layers
	 */
	private IndexImpl index;

	/**
	 * We need to track state - mainly to prevent access during initialisation
	 * 
	 * @author jens
	 * 
	 */
	private enum State {
		UNINITIALISED, INITIALISING, READY
	}

	private State currentState = State.UNINITIALISED;

	/**
	 * Constructor: creates index with specified size and file location
	 */
	public KeyValueBaseImpl(String filePath, long memorySize) {
		index = new IndexImpl(filePath, memorySize);
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

		// State-based error handling
		// If the object is already initialised, then throw an exception
		if (currentState == State.READY)
			throw new ServiceAlreadyInitializedException();

		// If a new request comes during initialisation, we need to raise an
		// exception
		else if (currentState == State.INITIALISING)
			throw new ServiceInitializingException();

		else {
			// set state to initialising immediately
			currentState = State.INITIALISING;

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						serverFilename));
				String line = null;
				String whitespace = "[ ]+";

				// handle first line:
				if ((line = reader.readLine()) != null) {
					String[] split = line.split(whitespace);
					if (split.length > 2)
						throw new ServiceInitializingException(
								"Badly formatted file - each line should contain a key (integer) and a value (integer)");

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

				System.out.println("Finished parsing input file " + serverFilename);
				currentState = State.READY;

			} catch (FileNotFoundException ex) {
				throw new FileNotFoundException(); // pass along to caller
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
				throw new ServiceInitializingException();
			} catch (KeyAlreadyPresentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// any clean up handled here - whether success or failure
				System.out.println("exiting init");
			}
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
			System.out.println("Inserting key " + k);
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

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		return null;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();

	}

	// /////////////////////////////////////////////////////////////////////////////////
	// UTILITY METHODS

	// Parse a string to a key
	private KeyImpl parseKey(String s) {
		int id = Integer.parseInt(s);
		return new KeyImpl(new Integer(id));
	}

	// Parse a string to value
	private ValueImpl parseValue(String s) {
		int value = Integer.parseInt(s);
		return new ValueImpl(value);
	}

}
