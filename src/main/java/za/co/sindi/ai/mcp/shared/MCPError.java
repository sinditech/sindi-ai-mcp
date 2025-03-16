package za.co.sindi.ai.mcp.shared;

/**
 * @author Buhake Sindi
 * @since 16 March 2025
 */
public class MCPError extends RuntimeException {

	private final int code;

	/**
	 * @param code
	 * @param message
	 */
	public MCPError(final int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
