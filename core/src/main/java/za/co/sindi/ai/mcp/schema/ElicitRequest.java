package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class ElicitRequest extends BaseRequest implements ServerRequest {

	public static final String METHOD_ELICITATION_CREATE = "elicitation/create";
	
	@JsonbProperty("params")
	private ElicitRequestParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public ElicitRequest(@JsonbProperty("params") ElicitRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "An elicity request parameters is required.");
	}

	public ElicitRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	@JsonbTypeInfo(
		key = "mode",
		value = {
		    @JsonbSubtype(alias="form", type=ElicitRequestFormParameters.class),
		    @JsonbSubtype(alias="url", type=ElicitRequestURLParameters.class),
		}
	)
	public static abstract class ElicitRequestParameters extends TaskAugmentedRequestParameters {
		
		private String message;
		
		/**
		 * @param message
		 */
		protected ElicitRequestParameters(String message) {
			super();
			this.message = Objects.requireNonNull(message, "A message is required.");
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
	}
	
	public static final class ElicitRequestFormParameters extends ElicitRequestParameters {
		
		private RequestedSchema requestedSchema;
		
		/**
		 * @param message
		 * @param requestedSchema
		 */
		@JsonbCreator
		public ElicitRequestFormParameters(@JsonbProperty("message") String message, @JsonbProperty("requestedSchema") RequestedSchema requestedSchema) {
			super(message);
			this.requestedSchema = Objects.requireNonNull(requestedSchema, "A requested schema is required.");
		}

		/**
		 * @return the requestedSchema
		 */
		public RequestedSchema getRequestedSchema() {
			return requestedSchema;
		}

		public static class RequestedSchema implements Serializable {
			
			@JsonbProperty("$schema")
			private String schema;
			
			@JsonbProperty
			private String type;
			
			@JsonbProperty
			private Map<String, PrimitiveSchemaDefinition> properties;
			
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
			 * @return the schema
			 */
			public String getSchema() {
				return schema;
			}

			/**
			 * @param schema the schema to set
			 */
			public void setSchema(String schema) {
				this.schema = schema;
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
			public Map<String, PrimitiveSchemaDefinition> getProperties() {
				return properties;
			}

			/**
			 * @param properties the properties to set
			 */
			public void setProperties(Map<String, PrimitiveSchemaDefinition> properties) {
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
	
	public static class ElicitRequestURLParameters extends ElicitRequestParameters {
		
		private String elicitationId;
		private String url;

		/**
		 * @param message
		 * @param elicitationId
		 * @param url
		 */
		@JsonbCreator
		public ElicitRequestURLParameters(@JsonbProperty("message") String message, @JsonbProperty("elicitationId") String elicitationId, @JsonbProperty("url") String url) {
			super(message);
			this.elicitationId = Objects.requireNonNull(elicitationId, "An elicitation ID is required.");
			this.url = Objects.requireNonNull(url, "A url is required.");
		}

		/**
		 * @return the elicitationId
		 */
		public String getElicitationId() {
			return elicitationId;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
	}
}
