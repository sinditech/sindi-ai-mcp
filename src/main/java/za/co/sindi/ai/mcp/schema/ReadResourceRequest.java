package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.ReadResourceRequest.ReadResourceRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ReadResourceRequest extends BaseRequest implements ParameterBasedRequest<ReadResourceRequestParameters>, ClientRequest {
	
	public static final String METHOD_READ_RESOURCE = "resources/read";
	
	@JsonbProperty("params")
	private ReadResourceRequestParameters parameters;

	public ReadResourceRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ReadResourceRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class ReadResourceRequestParameters extends BaseRequestParameters {
		
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
