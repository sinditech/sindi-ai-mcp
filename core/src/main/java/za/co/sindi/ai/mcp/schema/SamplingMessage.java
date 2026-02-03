package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class SamplingMessage implements Serializable {

	@JsonbProperty
	private Role role;
	
	@JsonbProperty
	private ContentBlock content;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the content
	 */
	public ContentBlock getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(ContentBlock content) {
		this.content = content;
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
