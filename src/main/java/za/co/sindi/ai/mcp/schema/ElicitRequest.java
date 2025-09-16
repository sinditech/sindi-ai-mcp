package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class ElicitRequest extends BaseRequest implements ServerRequest {

	public static final String METHOD_ELICITATION_CREATE = "elicitation/create";
	
	@JsonbProperty("params")
	private ElicitRequestParameters parameters;

	public ElicitRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ElicitRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class ElicitRequestParameters extends BaseRequestParameters {
		
		@JsonbProperty
		private String message;
		
		@JsonbProperty
		private RequestedSchema requestedSchema;
		
		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the requestedSchema
		 */
		public RequestedSchema getRequestedSchema() {
			return requestedSchema;
		}

		/**
		 * @param requestedSchema the requestedSchema to set
		 */
		public void setRequestedSchema(RequestedSchema requestedSchema) {
			this.requestedSchema = requestedSchema;
		}

		public static class RequestedSchema implements Serializable {
			
			@JsonbProperty
			private String type;
			
			@JsonbProperty
			private Map<String, ? extends PrimitiveSchemaDefinition> properties;
			
			@JsonbProperty
			private String[] required;

			/**
			 * 
			 */
			public RequestedSchema() {
				super();
				// TODO Auto-generated constructor stub
				setType("object");
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
			 * @return the properties
			 */
			public Map<String, ? extends PrimitiveSchemaDefinition> getProperties() {
				return properties;
			}

			/**
			 * @param properties the properties to set
			 */
			public void setProperties(Map<String, ? extends PrimitiveSchemaDefinition> properties) {
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
		}
	}
}
