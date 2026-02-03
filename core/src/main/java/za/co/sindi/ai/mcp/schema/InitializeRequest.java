package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
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
	
	/**
	 * @param parameters
	 */
	@JsonbCreator
	public InitializeRequest(@JsonbProperty("params") InitializeRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "An initialize request parameters are required.");
	}

	public InitializeRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class InitializeRequestParameters extends BaseRequestParameters {
		
		private ProtocolVersion protocolVersion;
		private ClientCapabilities capabilities;
		private Implementation clientInfo;

		/**
		 * @param protocolVersion
		 * @param capabilities
		 * @param clientInfo
		 */
		@JsonbCreator
		public InitializeRequestParameters(@JsonbProperty("protocolVersion") ProtocolVersion protocolVersion, @JsonbProperty("capabilities") ClientCapabilities capabilities,
				@JsonbProperty("clientInfo") Implementation clientInfo) {
			super();
			this.protocolVersion = Objects.requireNonNull(protocolVersion, "A protocol version is required.");
			this.capabilities = Objects.requireNonNull(capabilities, "A client capabilities is required.");
			this.clientInfo = Objects.requireNonNull(clientInfo, "A client info is required.");
		}

		/**
		 * @return the protocolVersion
		 */
		public ProtocolVersion getProtocolVersion() {
			return protocolVersion;
		}

		/**
		 * @return the capabilities
		 */
		public ClientCapabilities getCapabilities() {
			return capabilities;
		}

		/**
		 * @return the clientInfo
		 */
		public Implementation getClientInfo() {
			return clientInfo;
		}
	}
}
