package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListPromptsResult extends PaginatedResult implements ServerResult {

	@JsonbProperty
	private Prompt[] prompts;

	/**
	 * @return the prompts
	 */
	public Prompt[] getPrompts() {
		return prompts;
	}

	/**
	 * @param prompts the prompts to set
	 */
	public void setPrompts(Prompt[] prompts) {
		this.prompts = prompts;
	}
}
