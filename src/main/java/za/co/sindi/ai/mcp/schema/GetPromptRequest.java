package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.GetPromptRequest.GetPromptRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class GetPromptRequest extends BaseRequest implements ParameterBasedRequest<GetPromptRequestParameters>, ClientRequest {
	
	public static final String METHOD_PROMPTS_GET = "prompts/get";

	@JsonbProperty("params")
	private GetPromptRequestParameters parameters;

	public GetPromptRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(GetPromptRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class GetPromptRequestParameters implements Serializable {
		
		@JsonbProperty
		private String name;
		
		@JsonbProperty
		private Map<String, String> arguments;

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
