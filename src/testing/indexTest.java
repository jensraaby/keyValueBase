package testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

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
			
			try {
				System.out.print(testind.freeSpacesString());
				System.out.println("inserting with 2 keys: " + vl);
				int i, N;
				N = 10;
				ArrayList<KeyImpl> keys = new ArrayList<KeyImpl>();
				for (i = 1; i<N; i++) {
					KeyImpl newKey = new KeyImpl(i);
					testind.insert(newKey, vl);
					keys.add(newKey);
					System.out.print(testind.freeSpacesString());

				}
				ValueListImpl got = testind.get(keys.get(5));
				System.out.println(got);
				int size2 = vszl.toByteArray(got).length;
				System.out.println("size of valuelist before: " +  size1 + " after retrieval: " +  size2);
				
				long freeafter = testind.getFreeSpace();
				System.out.println("final freespace: " + freeafter + " difference: " + (freebefore - freeafter));

				
				vl.add(new ValueImpl(1124125626));
				vl.remove(v2);
				vl.remove(v);
				ValueListImpl gotBefore = testind.get(keys.get(3));
				testind.update(keys.get(3), vl);
				testind.update(keys.get(4), vl);
				System.out.println("size of updated valuelist : " +  vszl.toByteArray(vl).length);
				ValueListImpl gotAfter = testind.get(keys.get(3));		
				
				System.out.println("before value of key3: " + gotBefore + " after: " + gotAfter);
				
				System.out.println("final freespace after updating: " + testind.getFreeSpace());
				System.out.print("removing alternating keys: \n");
				testind.remove(keys.get(1));
				for (i = 1; i<N; i+=2) {
					KeyImpl newKey = new KeyImpl(i);
					System.out.println("removing key " + i);
					testind.remove(newKey);

				}
				
				System.out.print(testind.freeSpacesString());
				

				
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
