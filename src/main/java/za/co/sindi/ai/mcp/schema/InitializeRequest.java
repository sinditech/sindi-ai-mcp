package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.InitializeRequest.InitializeRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class InitializeRequest extends BaseRequest implements ParameterBasedRequest<InitializeRequestParameters>, ClientRequest {
	
	public static final String METHOD_INITIALIZE = "initialize";
	
	@JsonbProperty("params")
	private InitializeRequestParameters parameters;

	public InitializeRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(InitializeRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class InitializeRequestParameters extends BaseRequestParameters {
		
		@JsonbProperty
		private ProtocolVersion protocolVersion;
		
		@JsonbProperty
		private ClientCapabilities capabilities;
		
		@JsonbProperty
		private Implementation clientInfo;

		/**
		 * @return the protocolVersion
		 */
		public ProtocolVersion getProtocolVersion() {
			return protocolVersion;
		}

		/**
		 * @param protocolVersion the protocolVersion to set
		 */
		public void setProtocolVersion(ProtocolVersion protocolVersion) {
			this.protocolVersion = protocolVersion;
		}

		/**
		 * @return the capabilities
		 */
		public ClientCapabilities getCapabilities() {
			return capabilities;
		}

		/**
		 * @param capabilities the capabilities to set
		 */
		public void setCapabilities(ClientCapabilities capabilities) {
			this.capabilities = capabilities;
		}

		/**
		 * @return the clientInfo
		 */
		public Implementation getClientInfo() {
			return clientInfo;
		}

		/**
		 * @param clientInfo the clientInfo to set
		 */
		public void setClientInfo(Implementation clientInfo) {
			this.clientInfo = clientInfo;
		}
	}
}
