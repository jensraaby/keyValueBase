package testing;

import java.io.IOException;

import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import assignmentImplementation.IndexImpl;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.ValueImpl;
import assignmentImplementation.ValueListImpl;
import assignmentImplementation.ValueSerializerImpl;

public class indexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IndexImpl testind = new IndexImpl("testfile", 4096);
		ValueListImpl vl;
		try {
			vl = new ValueListImpl();
			ValueImpl v = new ValueImpl(88888);
			ValueImpl v2 = new ValueImpl(11111);
			vl.add(v);
			vl.add(v2);
			
			ValueSerializerImpl vszl = new ValueSerializerImpl();
			byte[] checksize = vszl.toByteArray(vl);
			int size1 = checksize.length;
			
			long freebefore = testind.getFreeSpace();
			
			System.out.println("initial freespace: " + freebefore);
			KeyImpl key = new KeyImpl(1);
			KeyImpl key2 = new KeyImpl(2);
			
			try {
				System.out.println("inserting with 2 keys: " + vl);
				testind.insert(key, vl);
				testind.insert(key2, vl);
				
				ValueListImpl got = testind.get(key);
				System.out.println(got);
				int size2 = vszl.toByteArray(got).length;
				System.out.println("size of valuelist before: " +  size1 + " after: " +  size2);
				
				long freeafter = testind.getFreeSpace();
				System.out.println("final freespace: " + freeafter + " difference: " + (freebefore - freeafter));

				testind.remove(key2);
				System.out.println("final freespace after deleting: " + testind.getFreeSpace());
				
				vl.add(new ValueImpl(1));
				vl.remove(v2);
				vl.remove(v);
				ValueListImpl gotBefore = testind.get(key);
				testind.update(key, vl);
				System.out.println("size of updated valuelist : " +  vszl.toByteArray(vl).length);
				ValueListImpl gotAfter = testind.get(key);		
				
				System.out.println("before value of key2: " + gotBefore + " after: " + gotAfter);
				
				System.out.println("final freespace after updating: " + testind.getFreeSpace());

				

				
			} catch (KeyNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (KeyAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
