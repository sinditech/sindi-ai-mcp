package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class GetPromptResult implements ServerResult {
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private PromptMessage[] messages;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the messages
	 */
	public PromptMessage[] getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(PromptMessage[] messages) {
		this.messages = messages;
	}
}
