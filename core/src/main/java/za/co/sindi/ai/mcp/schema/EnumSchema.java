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
	private String[] enums;
	
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
	 * @return the enums
	 */
	public String[] getEnums() {
		return enums;
	}

	/**
	 * @param enums the enums to set
	 */
	public void setEnums(String[] enums) {
		this.enums = enums;
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
