package assignmentImplementation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class KeyValueBaseImpl implements KeyValueBase<KeyImpl,ValueListImpl>
{

	public KeyValueBaseImpl(IndexImpl index) {
		// TODO Auto-generated constructor stub
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
		
		// check if file exists, if not throw filenotfoundexception
//		try (BufferedReader reader = Files.newBufferedReader(serverFilename, charset)) {
//		    String line = null;
//		    while ((line = reader.readLine()) != null) {
//		        System.out.println(line); // this is where to parse the line
//		    }
//		}
//	    catch (FileNotFoundException ex) {
//	    	throw FileNotFoundException; // pass back to caller
//	    catch (IOException x) {
//		    System.err.format("IOException: %s%n", x);
//		}
//	    catch (Exception ex) {
//	    	// catchall handler
//	    	throw ServiceInitializingException;
//	    }

	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

}
