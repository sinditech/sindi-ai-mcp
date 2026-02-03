package za.co.sindi.ai.mcp.schema;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
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

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public CompleteRequest(@JsonbProperty("params") CompleteRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A complete request parameter is required.");
	}

	public CompleteRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class CompleteRequestParameters extends BaseRequestParameters {
		
		private Reference reference;
		private Argument argument;
		
		@JsonbProperty
		private Context context;

		/**
		 * @param reference
		 * @param argument
		 */
		@JsonbCreator
		public CompleteRequestParameters(@JsonbProperty("ref") Reference reference, @JsonbProperty("argument") Argument argument) {
			super();
			this.reference = Objects.requireNonNull(reference, "A prompt or resource reference is required.");
			this.argument = Objects.requireNonNull(argument, "An argument is required.");
		}

		/**
		 * @return the reference
		 */
		public Reference getReference() {
			return reference;
		}

		/**
		 * @return the argument
		 */
		public Argument getArgument() {
			return argument;
		}

		/**
		 * @return the context
		 */
		public Context getContext() {
			return context;
		}

		/**
		 * @param context the context to set
		 */
		public void setContext(Context context) {
			this.context = context;
		}
	}
	
	public static final class Argument {
		
		@JsonbProperty
		private String name;
		
		@JsonbProperty
		private String value;

		/**
		 * @param name
		 * @param value
		 */
		public Argument(String name, String value) {
			super();
			this.name = Objects.requireNonNull(name, "Name is required.");
			this.value = Objects.requireNonNull(value, "Value is required.");
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}
	
	public static final class Context {
		
		@JsonbProperty
		private Map<String, String> arguments;

		/**
		 * @return the arguments
		 */
		public Map<String, String> getArguments() {
			return arguments;
		}

		/**
		 * @param arguments the arguments to set
		 */
		public void setArguments(Map<String, String> arguments) {
			this.arguments = arguments;
		}
	}
}
