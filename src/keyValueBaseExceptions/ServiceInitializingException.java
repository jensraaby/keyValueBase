package keyValueBaseExceptions;

public class ServiceInitializingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ServiceInitializingException (String message) {
		super(message);
	}
	
	public ServiceInitializingException () {
		super("Service is being initialized");
	}
}
