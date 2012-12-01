package testing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.Predicate;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.KeyValueBaseImpl;
import assignmentImplementation.ValueListImpl;

public class kvbtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String storePath = "teststore2";
		File store = new File(storePath);
		//store.deleteOnExit();
		
		KeyValueBaseImpl k = new KeyValueBaseImpl(storePath, 4096L);

		try {
			

			k.init("testinitdata");
			
			/***
			 * TEST SCAN 
			 */
			Predicate<ValueListImpl> p = new Predicate<ValueListImpl>() {

				@Override
				public boolean evaluate(ValueListImpl input) {
					// just returns all valuelistimpl
					return true;
				}
			};
			System.out.println("Scanning memory");
			List<ValueListImpl> scantest = k.scan(new KeyImpl(1), new KeyImpl(25), p);
			for (ValueListImpl vlist : scantest) {
				System.out.println(vlist);
			}
			
			System.out.println("test read (25): " + k.read(new KeyImpl(25)));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceInitializingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BeginGreaterThanEndException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
