/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class EnumSchema extends PrimitiveSchemaDefinition {

	@JsonbProperty("enum")
	private String[] _enum;
	
	@JsonbProperty
	private String[] enumNames;
	
	/**
	 * 
	 */
	public EnumSchema() {
		super();
		// TODO Auto-generated constructor stub
		setType("string");
	}

	/**
	 * @return the _enum
	 */
	public String[] getEnum() {
		return _enum;
	}

	/**
	 * @param _enum the _enum to set
	 */
	public void setEnum(String[] _enum) {
		this._enum = _enum;
	}

	/**
	 * @return the enumNames
	 */
	public String[] getEnumNames() {
		return enumNames;
	}

	/**
	 * @param enumNames the enumNames to set
	 */
	public void setEnumNames(String[] enumNames) {
		this.enumNames = enumNames;
	}
}
