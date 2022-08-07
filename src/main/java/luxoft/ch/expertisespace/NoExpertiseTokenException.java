package luxoft.ch.expertisespace;

public class NoExpertiseTokenException extends RuntimeException {

	public NoExpertiseTokenException(String message) {
		super(message);
	}

	public NoExpertiseTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
