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
		// default parameters
		super("teststore", 4096);
	}
	

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#init(java.lang.String)
	 */
	@Override @WebMethod
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		// TODO Auto-generated method stub
		super.init(serverFilename);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#read(assignmentImplementation.KeyImpl)
	 */
	@Override  @WebMethod
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.read(k);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#insert(assignmentImplementation.KeyImpl, assignmentImplementation.ValueListImpl)
	 */
	@Override  @WebMethod
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.insert(k, v);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#update(assignmentImplementation.KeyImpl, assignmentImplementation.ValueListImpl)
	 */
	@Override  @WebMethod
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.update(k, newV);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#delete(assignmentImplementation.KeyImpl)
	 */
	@Override  @WebMethod
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.delete(k);
	}

	/* (non-Javadoc)
	 * @see assignmentImplementation.KeyValueBaseImpl#scan(assignmentImplementation.KeyImpl, assignmentImplementation.KeyImpl, keyValueBaseInterfaces.Predicate)
	 */
	@Override  @WebMethod
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.scan(begin, end, p);
	}



}
