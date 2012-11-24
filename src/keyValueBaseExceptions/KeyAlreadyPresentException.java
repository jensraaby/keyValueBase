package keyValueBaseExceptions;

import keyValueBaseInterfaces.Key;

public class KeyAlreadyPresentException extends Exception {

	private static final long serialVersionUID = 1L;
	private Key<?> key = null;
	
	public KeyAlreadyPresentException (String message, Key<?> k) {
		super(message);
		key = k;
	}

	public KeyAlreadyPresentException (Key<?> k) {
		super("The key "+k+" is already present");
		key = k;
	}

	public Object getKey () {
		return key;
	}
}
