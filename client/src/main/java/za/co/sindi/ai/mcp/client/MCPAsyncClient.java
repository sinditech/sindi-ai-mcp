package za.co.sindi.ai.mcp.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
 * @since 21 February 2025
 */
public interface MCPAsyncClient {

	public CompletableFuture<Void> connectAsync();
	
	public CompletableFuture<InitializeResult> initializeAsync(final ClientCapabilities clientCapabilities, final Implementation clientInfo);
	
	public CompletableFuture<Void> pingAsync();
	
	public CompletableFuture<Resource[]> listResourcesAsync();
	
	public CompletableFuture<ResourceTemplate[]> listResourceTemplatesAsync();
	
	public CompletableFuture<ResourceContents[]> readResourceAsync(final String uri);
	
	public CompletableFuture<Void> subscribeResourceAsync(final String uri);
	
	public CompletableFuture<Void> unsubscribeResourceAsync(final String uri);
	
	public CompletableFuture<GetPromptResult> getPromptAsync(final String name, final Map<String, String> arguments);
	
	default CompletableFuture<Tool[]> listToolsAsync() {
		return listToolsAsync(null);
	}
	
	public CompletableFuture<Tool[]> listToolsAsync(final Cursor cursor);
	
	default CompletableFuture<CallToolResult> callToolAsync(final String name, final Map<String, Object> arguments) {
		return callToolAsync(name, arguments, null);
	}
	
	public CompletableFuture<CallToolResult> callToolAsync(final String name, final Map<String, Object> arguments, final ProgressHandler progressHandler);
	
	public CompletableFuture<Void> setLoggingLevelAsync(final LoggingLevel level);
	
	public CompletableFuture<Completion> completeAsync(final Reference reference, final Argument argument);
	
	public CompletableFuture<Void> addRootAsync(final Root root);
	
	public CompletableFuture<Void> removeRootAsync(final String uri);
}
