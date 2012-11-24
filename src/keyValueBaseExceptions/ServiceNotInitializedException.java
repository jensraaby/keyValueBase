package keyValueBaseExceptions;

public class ServiceNotInitializedException extends Exception {

	private static final long serialVersionUID = 1L;
	public ServiceNotInitializedException (String message) {
		super(message);
	}
	
	public ServiceNotInitializedException () {
		super("Service is not yet initialized");
	}
}
