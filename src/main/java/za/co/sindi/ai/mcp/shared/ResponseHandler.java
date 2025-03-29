package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.JSONRPCResult;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
public interface ResponseHandler {

	public void handle(JSONRPCResult result);
	
	public void handle(Throwable cause);
	
	public void handle(za.co.sindi.ai.mcp.schema.JSONRPCError.Error error);
}
