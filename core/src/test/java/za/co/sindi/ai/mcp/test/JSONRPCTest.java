package za.co.sindi.ai.mcp.test;

import java.util.Map;

import za.co.sindi.ai.mcp.mapper.JSONObjectMapper;
import za.co.sindi.ai.mcp.mapper.ObjectMapper;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeRequest.InitializeRequestParameters;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.JSONRPCNotification;
import za.co.sindi.ai.mcp.schema.JSONRPCRequest;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.ProgressNotification;
import za.co.sindi.ai.mcp.schema.ProgressNotification.ProgressNotificationParameters;
import za.co.sindi.ai.mcp.schema.ProgressToken;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.schema.RequestId;

/**
 * @author Buhake Sindi
 * @since 25 March 2025
 */
public class JSONRPCTest {

	public static void main(String[] args) {
		InitializeRequest request = new InitializeRequest(new InitializeRequestParameters(ProtocolVersion.getLatest(), new ClientCapabilities(), new Implementation("Test", "1.0.0")));
//		InitializeRequestParameters parameters = new InitializeRequestParameters(ProtocolVersion.getLatest(), new ClientCapabilities(), new Implementation("Test", "1.0.0"));
//		parameters.setCapabilities(new ClientCapabilities());
//		parameters.setClientInfo(new Implementation("Test", "1.0.0"));
//		parameters.setProtocolVersion(ProtocolVersion.getLatest());
//		request.setParameters(parameters);
		
		ObjectMapper mapper = JSONObjectMapper.newInstance();
		System.out.println(mapper.map(request));
		
		String data = "{\"jsonrpc\":\"2.0\",\"id\":0,\"method\":\"initialize\",\"params\":{\"capabilities\":{},\"clientInfo\":{\"name\":\"Test\",\"version\":\"1.0.0\"},\"protocolVersion\":\"2024-11-05\"}}";
		JSONRPCMessage object = MCPSchema.deserializeJSONRPCMessage(mapper, data);
		System.out.println(object.getClass());
		System.out.println("Done.");
		
		JSONRPCRequest jsonRequest = MCPSchema.toJSONRPCRequest(request);
		jsonRequest.setId(RequestId.of(0));
		jsonRequest.getParams().put("_meta", Map.of("progressToken", ProgressToken.of(0)));
		System.out.println(mapper.map(jsonRequest));
		
		ProgressNotificationParameters progressParameters = new ProgressNotificationParameters(ProgressToken.of("test"), 0);
		ProgressNotification notification = new ProgressNotification(progressParameters);
        JSONRPCNotification jsonNotification = MCPSchema.toJSONRPCNotification(notification);
        String notificationStr = mapper.map(jsonNotification);
        System.out.println(mapper.map(jsonNotification));
        
        JSONRPCNotification deser = (JSONRPCNotification) MCPSchema.deserializeJSONRPCMessage(notificationStr);
        ProgressNotification deserNotification = MCPSchema.toNotification(jsonNotification);
        System.out.println("Done!");
	}
}
