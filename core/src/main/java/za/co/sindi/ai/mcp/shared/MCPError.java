package za.co.sindi.ai.mcp.shared;

/**
 * @author Buhake Sindi
 * @since 16 March 2025
 */
public class MCPError extends RuntimeException {

	private final int code;
	private Object data;

	/**
	 * @param code
	 * @param message
	 */
	public MCPError(final int code, String message) {
		this(code, message, null);
	}
	
	/**
	 * @param code
	 * @param message
	 * @param data
	 */
	public MCPError(final int code, String message, Object data) {
		super(message);
		this.code = code;
		setData(data);
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	protected void setData(Object data) {
		this.data = data;
	}
}
