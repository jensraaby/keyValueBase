/**
 * 
 */
package webServices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import assignmentImplementation.KeyImpl;
import assignmentImplementation.KeyValueBaseImpl;
import assignmentImplementation.ValueImpl;
import assignmentImplementation.ValueListImpl;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Predicate;

/**
 * @author jens
 *
 */
@WebService
public class KVBServer extends KeyValueBaseImpl {

	public KVBServer()
	{
		super();
	}
	

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#init(java.lang.String)
	 */
	@WebMethod
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Initialising KVB server with file " + serverFilename);
		super.init(serverFilename);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#read(assignmentImplementation.KeyImpl)
	 */
	@WebMethod
	public ValueListImpl read(int k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.read(new KeyImpl(k));
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#insert(assignmentImplementation.KeyImpl, assignmentImplementation.ValueListImpl)
	 */
	@WebMethod
	public void insert(int k, int[] v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		
		ValueListImpl vl = new ValueListImpl();
		int i;
		for (i = 0; i < v.length; i++) {
			vl.add(new ValueImpl(v[i]));
		}
		super.insert(new KeyImpl(k), vl);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#update(assignmentImplementation.KeyImpl, assignmentImplementation.ValueListImpl)
	 */
	@WebMethod
	public void update(int k, int[] newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		ValueListImpl vl = new ValueListImpl();
		int i;
		for (i = 0; i < newV.length; i++) {
			vl.add(new ValueImpl(newV[i]));
		}
		super.update(new KeyImpl(k), vl);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#delete(assignmentImplementation.KeyImpl)
	 */
	@WebMethod
	public void delete(int k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		super.delete(new KeyImpl(k));
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#scan(assignmentImplementation.KeyImpl, assignmentImplementation.KeyImpl, keyValueBaseInterfaces.Predicate)
	 */
	@WebMethod
	public List<ValueListImpl> scan(int begin, int end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		return super.scan(new KeyImpl(begin), new KeyImpl(end), p);
	}



}
