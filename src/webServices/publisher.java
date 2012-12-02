package webServices;

import javax.xml.ws.Endpoint;


public class publisher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Endpoint.publish("http://localhost:8080/kvWS/", new KVBServer());
	}

}
