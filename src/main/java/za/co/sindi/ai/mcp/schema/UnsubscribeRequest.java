package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.UnsubscribeRequest.UnsubscribeRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class UnsubscribeRequest extends BaseRequest implements ParameterBasedRequest<UnsubscribeRequestParameters>, ClientRequest {
	
	public static final String METHOD_UNSUBSCRIBE = "resources/unsubscribe";
	
	@JsonbProperty("params")
	private UnsubscribeRequestParameters parameters;

	public UnsubscribeRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(UnsubscribeRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class UnsubscribeRequestParameters extends BaseRequestParameters {
		
		@JsonbProperty
		private String uri;

		/**
		 * @return the uri
		 */
		public String getUri() {
			return uri;
		}

		/**
		 * @param uri the uri to set
		 */
		public void setUri(String uri) {
			this.uri = uri;
		}
	}
}
