package serverTest;

import javax.xml.ws.Endpoint;

import keyValueBaseInterfaces.Index;

import assignmentImplementation.IndexImpl;
import assignmentImplementation.KeyValueBaseImpl;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		IndexImpl test = null;
		Endpoint.publish("http://localhost:1234/WS/kvb", new KeyValueBaseImpl());
	}

}
