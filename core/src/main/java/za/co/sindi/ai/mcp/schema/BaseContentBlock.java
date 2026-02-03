package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class BaseContentBlock {
	
	@JsonbProperty
	private Annotations annotations;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

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
