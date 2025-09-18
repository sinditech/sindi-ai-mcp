package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class ResourceTemplate extends BaseMetadata {

	@JsonbProperty
	private String uriTemplate;
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private String mimeType;
	
	@JsonbProperty
	private Annotations annotations;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

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

	/**
	 * @return the annotations
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(Annotations annotations) {
		this.annotations = annotations;
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
