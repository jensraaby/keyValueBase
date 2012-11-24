package keyValueBaseExceptions;

public class ServiceAlreadyInitializedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceAlreadyInitializedException (String message) {
		super(message);
	}

	public ServiceAlreadyInitializedException () {
		super("Service is already initialized");
	}
}
