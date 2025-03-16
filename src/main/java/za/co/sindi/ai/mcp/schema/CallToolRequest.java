package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CallToolRequest.CallToolRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CallToolRequest extends BaseRequest implements ParameterBasedRequest<CallToolRequestParameters>, ClientRequest {
	
	public static final String METHOD_TOOLS_CALL = "tools/call";
	
	/**
	 * 
	 */
	public CallToolRequest() {
		super(METHOD_TOOLS_CALL);
		//TODO Auto-generated constructor stub
	}

	@JsonbProperty("params")
	private CallToolRequestParameters parameters;

	public CallToolRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(CallToolRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class CallToolRequestParameters implements Serializable {
		
		@JsonbProperty
		private String name;
		
		@JsonbProperty
		private Map<String, Object> arguments;

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
