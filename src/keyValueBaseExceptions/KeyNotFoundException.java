package keyValueBaseExceptions;

import keyValueBaseInterfaces.Key;

public class KeyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private Key<?> key = null;
	
	public KeyNotFoundException (String message, Key<?> k) {
		super(message);
		key = k;
	}

	public KeyNotFoundException (Key<?> k) {
		super("The key "+k+" is not present");
		key = k;
	}

	public Object getKey () {
		return key;
	}
}