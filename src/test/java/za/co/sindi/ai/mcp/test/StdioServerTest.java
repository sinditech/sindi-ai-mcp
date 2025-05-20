/**
 * 
 */
package za.co.sindi.ai.mcp.test;

import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.server.DefaultMCPServer;
import za.co.sindi.ai.mcp.server.DefaultServer;
import za.co.sindi.ai.mcp.server.MCPServer;
import za.co.sindi.ai.mcp.server.Server;
import za.co.sindi.ai.mcp.server.StdioServerTransport;

/**
 * 
 */
public class StdioServerTest {

	public static void main(String[] args) {
		StdioServerTransport transport = new StdioServerTransport();
		Server server = new DefaultServer(transport, new Implementation("Server", "1.0.0"), new ServerCapabilities.Builder().enableAll().build(), null);
		server.setErrorHandler(throwable -> System.err.println(throwable));
		MCPServer mcpServer = new DefaultMCPServer(server);
		mcpServer.connect();
	}
}
