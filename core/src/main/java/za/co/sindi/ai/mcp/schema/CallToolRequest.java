package za.co.sindi.ai.mcp.schema;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CallToolRequest.CallToolRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CallToolRequest extends BaseRequest implements ParameterBasedRequest<CallToolRequestParameters>, ClientRequest {
	
	public static final String METHOD_TOOLS_CALL = "tools/call";
	
	@JsonbProperty("params")
	private CallToolRequestParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public CallToolRequest(@JsonbProperty("params") CallToolRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A call tool request parameters is required.");
	}

	public CallToolRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class CallToolRequestParameters extends TaskAugmentedRequestParameters {
		
		private String name;
		
		@JsonbProperty
		private Map<String, Object> arguments;

		/**
		 * @param name
		 */
		@JsonbCreator
		public CallToolRequestParameters(@JsonbProperty("name") String name) {
			super();
			this.name = Objects.requireNonNull(name, "A tool name is required.");
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the arguments
		 */
		public Map<String, Object> getArguments() {
			return arguments;
		}

		/**
		 * @param arguments the arguments to set
		 */
		public void setArguments(Map<String, Object> arguments) {
			this.arguments = arguments;
		}
	}
}
