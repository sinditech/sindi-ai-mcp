package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Resource extends BaseMetadata implements BaseIcons {

	@JsonbProperty
	private String uri;
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private String mimeType;
	
	@JsonbProperty
	private Long size;
	
	@JsonbProperty
	private Annotations annotations;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;
	
	@JsonbProperty
	private Icon[] icons;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
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
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
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

	/**
	 * @return the icons
	 */
	public Icon[] getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(Icon[] icons) {
		this.icons = icons;
	}
}
