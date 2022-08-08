package luxoft.ch.expertisespace.model;

public class NoExpertiseTokenException extends RuntimeException {

	public NoExpertiseTokenException(String message) {
		super(message);
	}

	public NoExpertiseTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
