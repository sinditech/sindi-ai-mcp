package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListRootsRequest extends BaseRequest implements ServerRequest {
	
	public static final String METHOD_ROOTS_LIST = "roots/list";

	/**
	 * 
	 */
	public ListRootsRequest() {
		super(METHOD_ROOTS_LIST);
		//TODO Auto-generated constructor stub
	}
}
