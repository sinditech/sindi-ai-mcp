package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class PromptArgument extends BaseMetadata {
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private Boolean required;

	/**
	 * 
	 */
	public PromptArgument() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param title
	 * @param description
	 * @param required
	 */
	public PromptArgument(String name, String title, String description, Boolean required) {
		super();
		setName(name);
		setTitle(title);
		this.description = description;
		this.required = required;
	}

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
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}
}
