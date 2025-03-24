package za.co.sindi.ai.mcp.server;

import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 25 March 2025
 */
public class DefaultServer extends Server {

	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities,
			String instructions) {
		super(transport, serverInfo, serverCapabilities, instructions);
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities) {
		super(transport, serverInfo, serverCapabilities);
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param transport
	 * @param serverInfo
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo) {
		super(transport, serverInfo);
		//TODO Auto-generated constructor stub
	}
}
