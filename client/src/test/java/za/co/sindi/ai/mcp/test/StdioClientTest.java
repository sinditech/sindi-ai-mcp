/**
 * 
 */
package za.co.sindi.ai.mcp.test;

import java.util.Arrays;

import za.co.sindi.ai.mcp.client.Client;
import za.co.sindi.ai.mcp.client.DefaultClient;
import za.co.sindi.ai.mcp.client.DefaultMCPClient;
import za.co.sindi.ai.mcp.client.MCPClient;
import za.co.sindi.ai.mcp.client.StdioClientTransport;
import za.co.sindi.ai.mcp.client.StdioServerParameters;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.Tool;

/**
 * 
 */
public class StdioClientTest {

	public static void main(String[] args) {
//		ServerParameters serverParameters = new ServerParameters("cmd", Arrays.asList("/k", "npx", "-y", "claude_gateway", "https://localhost:9080"), null);
		StdioServerParameters serverParameters = new StdioServerParameters("cmd", Arrays.asList("/k", "npx", "-y", "@modelcontextprotocol/server-filesystem@latest", "C:/Users/buhake.sindi/Desktop", "C:/Users/buhake.sindi/Downloads"), null);
		StdioClientTransport transport = new StdioClientTransport(serverParameters);
		Client client = new DefaultClient(transport, new ClientCapabilities.Builder().build(), new Implementation("Client", "1.0.0"));
		client.setErrorHandler(throwable -> System.err.println(throwable));
		MCPClient mcpClient = new DefaultMCPClient(client, null);
		mcpClient.connect();
		Tool[] tools = mcpClient.listTools();
	}
}
