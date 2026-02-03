package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.SubscribeRequest.SubscribeRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class SubscribeRequest extends BaseRequest implements ParameterBasedRequest<SubscribeRequestParameters>, ClientRequest {
	
	public static final String METHOD_SUBSCRIBE = "resources/subscribe";
	
	@JsonbProperty("params")
	private SubscribeRequestParameters parameters;

	public SubscribeRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(SubscribeRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class SubscribeRequestParameters extends BaseRequestParameters {
		
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
