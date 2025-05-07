package za.co.sindi.ai.mcp.client;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.shared.ClientTransport;

/**
 * @author Buhake Sindi
 * @since 24 March 2025
 */
public class DefaultClient extends Client {

	/**
	 * @param transport
	 * @param clientCapabilities
	 * @param clientInfo
	 */
	public DefaultClient(ClientTransport transport, ClientCapabilities clientCapabilities, Implementation clientInfo) {
		super(clientCapabilities, clientInfo);
		//TODO Auto-generated constructor stub
		setTransport(transport);
	}
}
