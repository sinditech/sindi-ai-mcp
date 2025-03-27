package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.SetLevelRequest.SetLevelRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class SetLevelRequest extends BaseRequest implements ParameterBasedRequest<SetLevelRequestParameters>, ClientRequest {
	
	public static final String METHOD_LOGGING_SETLEVEL = "logging/setLevel";

	@JsonbProperty("params")
	private SetLevelRequestParameters parameters;

	public SetLevelRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(SetLevelRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class SetLevelRequestParameters implements Serializable {
		
		@JsonbProperty
		private LoggingLevel level;

		/**
		 * @return the level
		 */
		public LoggingLevel getLevel() {
			return level;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(LoggingLevel level) {
			this.level = level;
		}
	}
}
