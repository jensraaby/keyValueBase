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

	
	public KeyValueBaseImpl() {
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
		try {
			BufferedReader reader = new BufferedReader(new FileReader(serverFilename));
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        System.out.println(line); // this is where to parse the line
		    }
		    reader.close();
		}
	    catch (FileNotFoundException ex) {
	    	throw new FileNotFoundException(); // pass back to caller
	    }
	    catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	    catch (Exception ex) {
	    	// catchall handler
	    	throw new ServiceInitializingException();
	    }
		finally {
			//any clean up handled here
			System.out.println("finished initialising!");
		}

	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@WebMethod
	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@WebMethod
	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@WebMethod
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
