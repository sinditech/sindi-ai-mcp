package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListResourcesRequest extends PaginatedRequest implements ClientRequest {

	public static final String METHOD_LIST_RESOURCES = "resources/list";

	/**
	 * 
	 */
	public ListResourcesRequest() {
		super(METHOD_LIST_RESOURCES);
		//TODO Auto-generated constructor stub
	}
}
