package assignmentImplementation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.KeyValueBase;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Predicate;


public class KeyValueBaseImpl implements KeyValueBase<KeyImpl,ValueListImpl>
{
	
	
	private IndexImpl index;
	private enum State {
	    UNINITIALISED, INITIALISING, READY
	}
	State currentState = State.UNINITIALISED;
	
	public KeyValueBaseImpl() {
		// TODO Auto-generated constructor stub
		index = new IndexImpl("memoryfile", 4096);
	}

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
	/**
	 * This function gets as input a path name to a file stored in the server containing
	 *  partial key-value mappings and initializes the key-value store. 
	 *  
	 *  It needs to check if the file exists; check that the service is not already active
	 *  Then parse the input file into the key-value store
	 */
	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		
		// State-based error handling 
		// If the object is already initialised, then throw an exception
		if (currentState == State.READY)
			throw new ServiceAlreadyInitializedException();
		
		// If a new request comes during initialisation, we need to raise an exception
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
				
				// first line:
				if ((line = reader.readLine()) != null) {
					String[] split = line.split(whitespace);
					if (split.length > 2)
						throw new ServiceInitializingException("Badly formatted file");
					
					KeyImpl key = parseKey(split[0]);
					ValueImpl value = parseValue(split[1]);
					ValueListImpl vl = new ValueListImpl();
					vl.add(value);
					// loop over remaining lines
					while ((line = reader.readLine()) != null) {
						
						split = line.split(whitespace);
						KeyImpl nextKey = parseKey(split[0]);
						ValueImpl nextValue = parseValue(split[1]);
						
						if (nextKey.compareTo(key) == 0)
						{
							// Same key as before, append the value
							vl.add(nextValue);
						}
						else {
							// insert the last key and start the next one
							index.insert(key, vl);
							System.out.println("Inserting key " + key + "values " + vl);
							key = nextKey;
							vl = new ValueListImpl();
							vl.add(nextValue);
							
							// if end of file, won't import this value
						}
						
					}
					// final value
					System.out.println("Inserting key " + key + "values " + vl);
					index.insert(key, vl);
				}
				
				reader.close();
				
				System.out.println("finished parsing input file!");
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
		
		return null;
	}

	@WebMethod
	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		
		System.out.println("Inserting key " + k);
		
	}

	@WebMethod
	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		
	}

	@WebMethod
	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		if (currentState != State.READY)
			throw new ServiceNotInitializedException();
		// TODO Auto-generated method stub
		return null;
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

}
