package testing;

import java.io.IOException;

import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.KeyValueBaseImpl;

public class kvbtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		KeyValueBaseImpl k = new KeyValueBaseImpl();
		
		
		
		try {
			KeyImpl k1 = new KeyImpl(1);
			KeyImpl k2 = new KeyImpl(2);
			if (k1.compareTo(k2) == 0)
				System.out.println("keys the same");
			else
				System.out.println("keys NOT the same");
			
			k.init("testinitdata");
//			k.read(new KeyImpl(1));
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceInitializingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
