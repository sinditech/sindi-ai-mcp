/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class IntegerNumberSchema extends PrimitiveSchemaDefinition {

	@JsonbProperty
	private Integer minimum;
	
	@JsonbProperty
	private Integer maximum;
	
	/**
	 * 
	 */
	public IntegerNumberSchema() {
		super();
		// TODO Auto-generated constructor stub
		setType("integer");
	}

	/**
	 * @return the minimum
	 */
	public Integer getMinimum() {
		return minimum;
	}

	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	/**
	 * @return the maximum
	 */
	public Integer getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
}
