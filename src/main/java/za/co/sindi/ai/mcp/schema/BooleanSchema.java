/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class BooleanSchema extends PrimitiveSchemaDefinition {

	@JsonbProperty("default")
	private Boolean _default;
	
	/**
	 * 
	 */
	public BooleanSchema() {
		super();
		// TODO Auto-generated constructor stub
		setType("boolean");
	}

	/**
	 * @return the default
	 */
	public Boolean getDefault() {
		return _default;
	}

	/**
	 * @param default1 the default to set
	 */
	public void setDefault(Boolean _default) {
		this._default = _default;
	}
}
