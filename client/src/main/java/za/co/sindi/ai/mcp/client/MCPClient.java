package za.co.sindi.ai.mcp.client;

import java.util.Map;

import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.CompleteRequest.Argument;
import za.co.sindi.ai.mcp.schema.CompleteResult.Completion;
import za.co.sindi.ai.mcp.schema.Cursor;
import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.LoggingLevel;
import za.co.sindi.ai.mcp.schema.Reference;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.schema.ResourceContents;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.schema.Root;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.shared.ProgressHandler;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public interface MCPClient extends AutoCloseable {
	
	public void connect();

	public InitializeResult initialize(final ClientCapabilities clientCapabilities, final Implementation clientInfo);
	
	public void ping();
	
	public Resource[] listResources();
	
	public ResourceTemplate[] listResourceTemplates();
	
	public ResourceContents[] readResource(final String uri);
	
	public void subscribeResource(final String uri);
	
	public void unsubscribeResource(final String uri);
	
	public GetPromptResult getPrompt(final String name, final Map<String, String> arguments);
	
	default Tool[] listTools() {
		return listTools(null);
	}
	
	public Tool[] listTools(final Cursor cursor);
	
	default CallToolResult callTool(final String name, final Map<String, Object> arguments) {
		return callTool(name, arguments, null);
	}
	
	public CallToolResult callTool(final String name, final Map<String, Object> arguments, final ProgressHandler progressHandler);
	
	public void setLoggingLevel(final LoggingLevel level);
	
	public Completion complete(final Reference reference, final Argument argument);
	
	public void addRoot(final Root root);
	
	public void removeRoot(final String uri);
}
