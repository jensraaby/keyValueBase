package testing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseExceptions.ServiceAlreadyInitializedException;
import keyValueBaseExceptions.ServiceInitializingException;
import keyValueBaseExceptions.ServiceNotInitializedException;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.KeyValueBaseImpl;
import assignmentImplementation.ValueImpl;
import assignmentImplementation.ValueListImpl;

public class readtest {
	
	public static void main(String[] args) {
		
		KeyValueBaseImpl k = new KeyValueBaseImpl();
	

		
		int i;

        try {
        	
			k.init("epinions");
			
//			for (i=1; i <= 5; i++) {
//				Thread rd = new Thread(reader);
//				rd.start();
//			}
			
			
			for (i=1; i <= 128; i++) {
				Thread zrd = new Thread(zipfreader);
				zrd.start();
			}
			
			
			System.out.println("starting ...");
			

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceAlreadyInitializedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceInitializingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private static Runnable reader = new Runnable() {
		KeyValueBaseImpl kvb = new KeyValueBaseImpl();
		@Override
		public void run() {
			int i;
			long start; 
	        long stop;

	        
	        start= new Date().getTime();
	        
			for (i = 1; i<=545; i++)
			{
				try {
					
					
					kvb.read(new KeyImpl(i));
					
				
				} catch (IOException e) {
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
			stop = new Date().getTime();
			System.out.println(" TIME (ms) : "+((double)(stop-start))/545);
			
		}
	};
	

	private static Runnable zipfreader = new Runnable() {
		KeyValueBaseImpl kvb = new KeyValueBaseImpl();
		@Override
		public void run() {
			int i;
			long start; 
	        long stop;

	        
			ZipfGenerator zipf = new ZipfGenerator(Integer.valueOf(545),Double.valueOf(0.8));
			

			int[] a = new int[545];
			
			for (int j = 1; j < 545; j++){

				a[j] = 		zipf.next()+1 ;
			}

	        
	        start= new Date().getTime();
	        
			for (i = 1; i < 545; i++)
			{
				try {
					
					
					kvb.read(new KeyImpl(a[i]));
					
				
				} catch (IOException e) {
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
			stop= new Date().getTime();
			System.out.println( +((double)1000/(((double)(stop-start))/544)));
			
		}
	};

}
