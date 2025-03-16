package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ErrorCodes {
	
	// SDK error codes
	public static final int CONNECTION_CLOSED = -32000;
	public static final int REQUEST_TIMEOUT = -32001;

	//Standard JSON-RPC error codes
	public static final int PARSE_ERROR = -32700;
	public static final int INVALID_REQUEST = -32600;
	public static final int METHOD_NOT_FOUND = -32601;
	public static final int INVALID_PARAMS = -32602;
	public static final int INTERNAL_ERROR = -32603;
	
	private ErrorCodes() {
		throw new AssertionError("Private constructor.");
	}
}
