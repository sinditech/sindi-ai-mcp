package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class ResourceTemplate extends Annotated {

	@JsonbProperty
	private String uriTemplate;
	
	@JsonbProperty
	private String name;
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private String mimeType;

	/**
	 * @return the uriTemplate
	 */
	public String getUriTemplate() {
		return uriTemplate;
	}

	/**
	 * @param uriTemplate the uriTemplate to set
	 */
	public void setUriTemplate(String uriTemplate) {
		this.uriTemplate = uriTemplate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
