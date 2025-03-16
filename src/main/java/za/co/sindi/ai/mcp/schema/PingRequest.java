package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class PingRequest extends BaseRequest implements ClientRequest, ServerRequest {
	
	public static final String METHOD_PING = "ping";

	/**
	 * 
	 */
	public PingRequest() {
		super(METHOD_PING);
		//TODO Auto-generated constructor stub
	}
}
