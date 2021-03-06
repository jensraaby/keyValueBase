package testing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import keyValueBaseInterfaces.Predicate;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.KeyValueBaseImpl;
import assignmentImplementation.ValueImpl;
import assignmentImplementation.ValueListImpl;

public class kvbtest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		// check that multiple instances share same storage:
		KeyValueBaseImpl k = new KeyValueBaseImpl();
		KeyValueBaseImpl k2 = new KeyValueBaseImpl();
		
		try {
			

			k.init("testinitdata");
			System.out.println(" first read" + k2.read(new KeyImpl(24)));
			/***
			 * TEST SCAN 
			 */
			Predicate<ValueListImpl> p = new Predicate<ValueListImpl>() {

				@Override
				public boolean evaluate(ValueListImpl input) {
//					return (input.equals(new ValueImpl(25)));
					return true;
				}
			};
			System.out.println("Scanning memory");
			List<ValueListImpl> scantest = k.atomicScan(new KeyImpl(1), new KeyImpl(25), p);
			for (ValueListImpl vlist : scantest) {
				System.out.println(vlist);
			}
			
			System.out.println("test read (25): " + k.read(new KeyImpl(25)));
			
			// try another thread which will try to add a load of keys
			Thread writer = new Thread(inserter);
			Thread writer2 = new Thread(inserter);

			Thread changer = new Thread(updater);
			Thread changer2 = new Thread(updater);

			System.out.println("Starting threads");
			writer.start();
			changer.start();
//			writer2.start();
			Thread.sleep(20);
			changer2.start();
			
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

	private static Runnable inserter = new Runnable() {
		KeyValueBaseImpl kvb = new KeyValueBaseImpl();
		@Override
		public void run() {
			int i;
			for (i = 30; i<1000; i++)
			{
				try {
					if (i % 10 == 0) {
						
						System.out.println("inserting key " + i);
					}
					ValueListImpl val = new ValueListImpl();
					val.add(new ValueImpl(i));
					kvb.insert(new KeyImpl(i), val);
//					kvb.update(new KeyImpl(i), val);
					
				} catch (KeyAlreadyPresentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceNotInitializedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};
	
	private static Runnable updater = new Runnable() {
		KeyValueBaseImpl kvb = new KeyValueBaseImpl();
		@Override
		public void run() {
			System.out.println("running updater");
			int i;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (i = 30; i<1000; i++)
			{
				try {
					ValueListImpl oldvalue = kvb.read(new KeyImpl(i));
					if (i % 10 == 0) {
						Thread.sleep(200);
						System.out.println("val for " + i + " is: " + oldvalue);
					}
					
					
					ValueListImpl newValue = new ValueListImpl();
					newValue.add(new ValueImpl(2*i));
					kvb.update(new KeyImpl(i), newValue);
					ValueListImpl val = kvb.read(new KeyImpl(i));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceNotInitializedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};
}
