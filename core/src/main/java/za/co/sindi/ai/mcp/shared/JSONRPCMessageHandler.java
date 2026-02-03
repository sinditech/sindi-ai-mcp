package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;

/**
 * @author Buhake Sindi
 * @since 20 February 2025
 */
public interface JSONRPCMessageHandler {

	public void onMessage(final JSONRPCMessage message);
	
	public void onError(final Throwable throwable);
	
	public void onClose();
}
