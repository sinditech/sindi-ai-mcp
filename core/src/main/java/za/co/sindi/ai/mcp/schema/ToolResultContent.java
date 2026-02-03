package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public final class ToolResultContent implements SamplingMessageContentBlock {

	@JsonbProperty
	private String toolUseId;
	
	@JsonbProperty
	private ContentBlock[] content;

	@JsonbProperty
	private Map<String, Object> structuredContent;
	
	@JsonbProperty("isError")
	private Boolean error;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

	/**
	 * 
	 */
	public ToolResultContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param toolUseId
	 * @param content
	 */
	public ToolResultContent(String toolUseId, ContentBlock[] content) {
		super();
		this.toolUseId = toolUseId;
		this.content = content;
	}

	/**
	 * @return the toolUseId
	 */
	public String getToolUseId() {
		return toolUseId;
	}

	/**
	 * @param toolUseId the toolUseId to set
	 */
	public void setToolUseId(String toolUseId) {
		this.toolUseId = toolUseId;
	}

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
	 * @return the structuredContent
	 */
	public Map<String, Object> getStructuredContent() {
		return structuredContent;
	}

	/**
	 * @param structuredContent the structuredContent to set
	 */
	public void setStructuredContent(Map<String, Object> structuredContent) {
		this.structuredContent = structuredContent;
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

	/**
	 * @return the meta
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
}
