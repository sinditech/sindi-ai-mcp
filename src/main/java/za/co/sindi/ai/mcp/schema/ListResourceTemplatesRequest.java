package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListResourceTemplatesRequest extends PaginatedRequest implements ClientRequest {

	public static final String METHOD_LIST_RESOURCE_TEMPLATES = "resources/templates/list";

	/**
	 * 
	 */
	public ListResourceTemplatesRequest() {
		super(METHOD_LIST_RESOURCE_TEMPLATES);
		//TODO Auto-generated constructor stub
	}
}
