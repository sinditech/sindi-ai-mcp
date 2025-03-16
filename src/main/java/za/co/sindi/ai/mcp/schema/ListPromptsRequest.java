package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListPromptsRequest extends PaginatedRequest implements ClientRequest {

	public static final String METHOD_LIST_PROMPTS = "prompts/list";

	/**
	 * 
	 */
	public ListPromptsRequest() {
		super(METHOD_LIST_PROMPTS);
		//TODO Auto-generated constructor stub
	}
}
