/**
 * 
 */
package za.co.sindi.ai.mcp.client;

/**
 * @author Buhake Sindi
 * @since 25 April 2025
 */
public class SseError extends RuntimeException {

	private final int code;

	/**
	 * @param message
	 * @param code
	 */
	public SseError(String message, int code) {
		super("SSE Error error: "+ message);
		this.code = code;
	}

	/**
	 * @param cause
	 * @param code
	 */
	public SseError(Throwable cause, int code) {
		super(cause);
		this.code = code;
	}

	/**
	 * @param message
	 * @param cause
	 * @param code
	 */
	public SseError(String message, Throwable cause, int code) {
		super("SSE Error error: "+ message, cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
