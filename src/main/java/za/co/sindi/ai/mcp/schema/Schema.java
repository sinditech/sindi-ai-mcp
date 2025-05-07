package za.co.sindi.ai.mcp.schema;

/**
 *  @author Buhake Sindi
 * 	@since 08 February 2025
 */
public final class Schema {

	public static final String LATEST_PROTOCOL_VERSION = "2025-03-26";
	public static final String JSONRPC_VERSION = "2.0";
	public static final EmptyResult EMPTY_RESULT = new EmptyResult();
	
	private Schema() {
		throw new AssertionError("Private constructor.");
	}
}
