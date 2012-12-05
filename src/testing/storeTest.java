package testing;

import java.io.File;

import assignmentImplementation.StoreImpl;

public class storeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String path = "tempfile";
		
			/**
			 *  This just creates a store (in project dir), then writes some bytes
			 *  Then we check that the read bytes match up
			 */
			StoreImpl store = new StoreImpl(path, 2048);
			
			
			byte[] testwrite = {-100,10,1,2,3};
			for (int i = 0; i< testwrite.length; i++) {
				System.out.print(testwrite[i] + ", ");
				
			}
			System.out.println();
			System.out.println("created store, writing bytes");
	
			store.write((long) 0,testwrite);
	
			byte[] testread = store.read((long) 0, testwrite.length);
			
			System.out.println("read: length: " + testread.length);
			for (int i = 0; i< testread.length; i++) {
				System.out.print(testread[i] + ", ");
			}
			File f = new File(path);
			f.delete();
	
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
