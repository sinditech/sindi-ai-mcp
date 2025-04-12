package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Tool implements Serializable {

	@JsonbProperty
	private String name;
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private InputSchema inputSchema;
	
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
	 * @return the inputSchema
	 */
	public InputSchema getInputSchema() {
		return inputSchema;
	}

	/**
	 * @param inputSchema the inputSchema to set
	 */
	public void setInputSchema(InputSchema inputSchema) {
		this.inputSchema = inputSchema;
	}

	public static class InputSchema implements Serializable {
		
		@JsonbProperty
		private String type;
		
		@JsonbProperty
		private Map<String, PropertyInfo> properties;
		
		@JsonbProperty
		private String[] required;

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the properties
		 */
		public Map<String, PropertyInfo> getProperties() {
			return properties;
		}

		/**
		 * @param properties the properties to set
		 */
		public void setProperties(Map<String, PropertyInfo> properties) {
			this.properties = properties;
		}

		/**
		 * @return the required
		 */
		public String[] getRequired() {
			return required;
		}

		/**
		 * @param required the required to set
		 */
		public void setRequired(String[] required) {
			this.required = required;
		}
		
		public static class PropertyInfo {
			
			@JsonbProperty
			private String type;
			
			@JsonbProperty
			private String description;

			/**
			 * 
			 */
			public PropertyInfo() {
				super();
				// TODO Auto-generated constructor stub
			}

			/**
			 * @param type
			 * @param description
			 */
			public PropertyInfo(String type, String description) {
				super();
				this.type = type;
				this.description = description;
			}

			/**
			 * @return the type
			 */
			public String getType() {
				return type;
			}

			/**
			 * @param type the type to set
			 */
			public void setType(String type) {
				this.type = type;
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
		}
	}
}
