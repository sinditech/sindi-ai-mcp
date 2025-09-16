package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CallToolResult implements ServerResult {
	
	@JsonbProperty
	private ContentBlock[] content;
	
	@JsonbProperty("isError")
	private Boolean error;

	/**
	 * @return the content
	 */
	public ContentBlock[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(ContentBlock[] content) {
		this.content = content;
	}

	/**
	 * @return the error
	 */
	public Boolean getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Boolean error) {
		this.error = error;
	}
}
