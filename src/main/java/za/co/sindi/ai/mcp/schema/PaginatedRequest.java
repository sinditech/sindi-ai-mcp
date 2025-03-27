package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.PaginatedRequest.PaginatedRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class PaginatedRequest extends BaseRequest implements ParameterBasedRequest<PaginatedRequestParameters> {
	
	@JsonbProperty("params")
	private PaginatedRequestParameters parameters;

	/**
	 * @return the parameters
	 */
	public PaginatedRequestParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(PaginatedRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class PaginatedRequestParameters implements Serializable {
		
		@JsonbProperty
		private String cursor;

		/**
		 * @return the cursor
		 */
		public String getCursor() {
			return cursor;
		}

		/**
		 * @param cursor the cursor to set
		 */
		public void setCursor(String cursor) {
			this.cursor = cursor;
		}
	}
}
