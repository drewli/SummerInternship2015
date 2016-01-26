package exceptions;

public class IECrashException extends RuntimeException {
	private static final long serialVersionUID = 7526472295622776147L;  // unique id

	public IECrashException() {
		super();
	}

	public IECrashException(String message) {
		super(message);
	}

	public IECrashException(Throwable cause) {
		super(cause);
	}

	public IECrashException(String message, Throwable cause) {
		super(message, cause);
	}

	public IECrashException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
