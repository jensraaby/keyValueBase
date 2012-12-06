package webServices.client;

import java.util.Date;
import java.util.List;

import keyValueBaseInterfaces.Predicate;
import assignmentImplementation.KeyImpl;
import assignmentImplementation.ValueListImpl;




public class SimpleKVBClient {

	KVBServerService service = null;
	
	public SimpleKVBClient () {
		
		service = new KVBServerService() ;
	}
	
	public void testInit (){
		KVBServer servicePort = service.getKVBServerPort();
		long start; 
        long stop;
		
		try {
	        start= new Date().getTime();
			servicePort.init("testinitdata");
			stop= new Date().getTime();
			
			System.out.println(" TIME (ms) : "+(stop-start));
			
			System.out.println("Initialized by the Client");
			
			
			
		} catch (FileNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void testRead () {
		KVBServer servicePort = service.getKVBServerPort();
		 
		
		try {
			webServices.client.ValueListImpl vv = servicePort.read(new webServices.client.KeyImpl(26));
			//System.out.println(" JUST READ : " +vv.getVlist());
			
			//webServices.client.ValueListImpl a = null;
			
			servicePort.insert(new webServices.client.KeyImpl(333), new webServices.client.ValueListImpl());
			//servicePort.insert(0, a);
			
			
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyAlreadyPresentException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	public static void main (String arags[]){
		SimpleKVBClient client = new SimpleKVBClient();
	
		client.testInit() ;
		
		client.testRead() ;

	}
	
}