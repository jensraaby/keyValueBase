package testing;

import assignmentImplementation.KeyImpl;

public class keyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KeyImpl test1 = new KeyImpl(2);
		KeyImpl test2 = new KeyImpl(3);
		KeyImpl test3 = new KeyImpl(3);
		
	   System.out.println(test1.compareTo(test2));
	   System.out.println(test3.compareTo(test1));
	   System.out.println(test2.compareTo(test3));
	}

}
