/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public class RequestMeta implements Serializable {

	@JsonbProperty
	private ProgressToken progressToken;

	/**
	 * @return the progressToken
	 */
	public ProgressToken getProgressToken() {
		return progressToken;
	}

	/**
	 * @param progressToken the progressToken to set
	 */
	public void setProgressToken(ProgressToken progressToken) {
		this.progressToken = progressToken;
	}
}
