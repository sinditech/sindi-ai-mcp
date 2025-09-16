package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CompleteRequest.CompleteRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CompleteRequest extends BaseRequest implements ParameterBasedRequest<CompleteRequestParameters>, ClientRequest {
	
	public static final String METHOD_COMPLETION_COMPLETE = "completion/complete";
	
	@JsonbProperty("params")
	private CompleteRequestParameters parameters;

	public CompleteRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(CompleteRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class CompleteRequestParameters extends BaseRequestParameters {
		
		@JsonbProperty("ref")
		private Reference reference;
		
		@JsonbProperty
		private Argument argument;

		/**
		 * @return the reference
		 */
		public Reference getReference() {
			return reference;
		}

		/**
		 * @param reference the reference to set
		 */
		public void setReference(Reference reference) {
			this.reference = reference;
		}

		/**
		 * @return the argument
		 */
		public Argument getArgument() {
			return argument;
		}

		/**
		 * @param argument the argument to set
		 */
		public void setArgument(Argument argument) {
			this.argument = argument;
		}
	}
	
	public static final class Argument {
		
		@JsonbProperty
		private String name;
		
		@JsonbProperty
		private String value;

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
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}
}
