/**
 * 
 */
package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.RequestId;
import za.co.sindi.ai.mcp.schema.RequestMeta;

/**
 * @author Buhake Sindi
 * @since 16 April 2025
 */
public class RequestHandlerExtra {

	/**
     * The session ID from the transport, if available.
     */
	private String sessionId;
	
	/**
     * Metadata from the original request.
     */
	private RequestMeta meta;
	
	/**
     * The JSON-RPC ID of the request being handled.
     * This can be useful for tracking or logging purposes.
     */
	private RequestId requestId;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the meta
	 */
	public RequestMeta getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(RequestMeta meta) {
		this.meta = meta;
	}

	/**
	 * @return the requestId
	 */
	public RequestId getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(RequestId requestId) {
		this.requestId = requestId;
	}
}
