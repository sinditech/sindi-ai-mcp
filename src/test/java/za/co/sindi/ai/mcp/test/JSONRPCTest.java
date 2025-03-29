package za.co.sindi.ai.mcp.test;

import za.co.sindi.ai.mcp.client.Client;
import za.co.sindi.ai.mcp.client.DefaultClient;
import za.co.sindi.ai.mcp.client.DefaultMCPClient;
import za.co.sindi.ai.mcp.client.MCPClient;
import za.co.sindi.ai.mcp.client.SSEClientTransport;
import za.co.sindi.ai.mcp.mapper.JSONObjectMapper;
import za.co.sindi.ai.mcp.mapper.ObjectMapper;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeRequest.InitializeRequestParameters;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;

/**
 * @author Buhake Sindi
 * @since 25 March 2025
 */
public class JSONRPCTest {

	public static void main(String[] args) {
		InitializeRequest request = new InitializeRequest();
		InitializeRequestParameters parameters = new InitializeRequestParameters();
		parameters.setCapabilities(new ClientCapabilities());
		parameters.setClientInfo(new Implementation("Test", "1.0.0"));
		parameters.setProtocolVersion(ProtocolVersion.getLatest());
		request.setParameters(parameters);
		
		ObjectMapper mapper = JSONObjectMapper.newInstance();
		System.out.println(mapper.map(request));
		
		String data = "{\"jsonrpc\":\"2.0\",\"id\":0,\"method\":\"initialize\",\"params\":{\"capabilities\":{},\"clientInfo\":{\"name\":\"Test\",\"version\":\"1.0.0\"},\"protocolVersion\":\"2024-11-05\"}}";
		JSONRPCMessage object = MCPSchema.deserializeJSONRPCMessage(mapper, data);
		System.out.println(object.getClass());
		System.out.println("Done.");
		
		SSEClientTransport transport = new SSEClientTransport("http://localhost:9080");
		Client client = new DefaultClient(transport, new ClientCapabilities.Builder().build(), new Implementation("Client", "1.0.0"));
		MCPClient mcpClient = new DefaultMCPClient(client, null);
		((DefaultMCPClient)mcpClient).connect();
	}
}
