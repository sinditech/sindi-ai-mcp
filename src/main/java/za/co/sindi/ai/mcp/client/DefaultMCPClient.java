package za.co.sindi.ai.mcp.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import za.co.sindi.ai.mcp.schema.CallToolRequest;
import za.co.sindi.ai.mcp.schema.CallToolRequest.CallToolRequestParameters;
import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.CompleteRequest;
import za.co.sindi.ai.mcp.schema.CompleteRequest.Argument;
import za.co.sindi.ai.mcp.schema.CompleteRequest.CompleteRequestParameters;
import za.co.sindi.ai.mcp.schema.CompleteResult;
import za.co.sindi.ai.mcp.schema.CompleteResult.Completion;
import za.co.sindi.ai.mcp.schema.EmptyResult;
import za.co.sindi.ai.mcp.schema.GetPromptRequest;
import za.co.sindi.ai.mcp.schema.GetPromptRequest.GetPromptRequestParameters;
import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeRequest.InitializeRequestParameters;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesRequest;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesResult;
import za.co.sindi.ai.mcp.schema.ListResourcesRequest;
import za.co.sindi.ai.mcp.schema.ListResourcesResult;
import za.co.sindi.ai.mcp.schema.ListRootsRequest;
import za.co.sindi.ai.mcp.schema.ListRootsResult;
import za.co.sindi.ai.mcp.schema.ListToolsRequest;
import za.co.sindi.ai.mcp.schema.ListToolsResult;
import za.co.sindi.ai.mcp.schema.LoggingLevel;
import za.co.sindi.ai.mcp.schema.PaginatedRequest.PaginatedRequestParameters;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.schema.ReadResourceRequest;
import za.co.sindi.ai.mcp.schema.ReadResourceRequest.ReadResourceRequestParameters;
import za.co.sindi.ai.mcp.schema.ReadResourceResult;
import za.co.sindi.ai.mcp.schema.Reference;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.schema.ResourceContents;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.schema.Root;
import za.co.sindi.ai.mcp.schema.RootsListChangedNotification;
import za.co.sindi.ai.mcp.schema.SetLevelRequest;
import za.co.sindi.ai.mcp.schema.SetLevelRequest.SetLevelRequestParameters;
import za.co.sindi.ai.mcp.schema.SubscribeRequest;
import za.co.sindi.ai.mcp.schema.SubscribeRequest.SubscribeRequestParameters;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.schema.UnsubscribeRequest;
import za.co.sindi.ai.mcp.schema.UnsubscribeRequest.UnsubscribeRequestParameters;
import za.co.sindi.ai.mcp.shared.Client;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.RequestHandler;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 21 February 2025
 */
public class DefaultMCPClient extends Client implements MCPAsyncClient, MCPClient {
	
	private final ConcurrentHashMap<String, Root> roots = new ConcurrentHashMap<>();

	/**
	 * @param transport
	 * @param clientCapabilities
	 * @param clientInfo
	 */
	public DefaultMCPClient(ClientTransport transport, ClientCapabilities clientCapabilities, Implementation clientInfo) {
		super(transport, clientCapabilities, clientInfo);
		
		if (clientCapabilities.getRoots() != null) {
			addRequestHandler(ListRootsRequest.METHOD_ROOTS_LIST, listRootsRequestHandler());
			addNotificationHandler(RootsListChangedNotification.METHOD_NOTIFICATION_ROOTS_lIST_CHANGED, notification -> {});
		}
		
		if (clientCapabilities.getSampling() != null) {
			
		}
	}
	
	private RequestHandler<ListRootsResult> listRootsRequestHandler() {
		
		return request -> {
			ListRootsResult result = new ListRootsResult();
			result.setRoots(roots.values().toArray(new Root[roots.size()]));
			return result;
		};
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Protocol#connect()
	 */
	
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		super.connect();
		
		InitializeResult result = initialize();
		Preconditions.checkState(result != null, "Server sent invalid initialize result.");
		
		LOGGER.info("Server protocol version: " + result.getProtocolVersion());
		
		serverCapabilities = result.getCapabilities();
		serverInfo = result.getServerInfo();
		instructions = result.getInstructions();
		
		//Notify
		sendNotification(new InitializedNotification());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#initialize()
	 */
	@Override
	public InitializeResult initialize() {
		// TODO Auto-generated method stub
		try {
			return initializeAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#ping()
	 */
	@Override
	public EmptyResult ping() {
		// TODO Auto-generated method stub
		try {
			return pingAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#listResources()
	 */
	@Override
	public Resource[] listResources() {
		// TODO Auto-generated method stub
		try {
			return listResourcesAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#listResourceTemplates()
	 */
	@Override
	public ResourceTemplate[] listResourceTemplates() {
		// TODO Auto-generated method stub
		try {
			return listResourceTemplatesAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#readResource(java.lang.String)
	 */
	@Override
	public ResourceContents[] readResource(String uri) {
		// TODO Auto-generated method stub
		try {
			return readResourceAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#subscribe(java.lang.String)
	 */
	@Override
	public void subscribeResource(String uri) {
		// TODO Auto-generated method stub
		try {
			subscribeResourceAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribeResource(String uri) {
		// TODO Auto-generated method stub
		try {
			unsubscribeResourceAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#getPrompt(java.lang.String, java.util.Map)
	 */
	@Override
	public GetPromptResult getPrompt(String name, Map<String, String> arguments) {
		// TODO Auto-generated method stub
		try {
			return getPromptAsync(name, arguments).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#listTools()
	 */
	@Override
	public Tool[] listTools() {
		// TODO Auto-generated method stub
		try {
			return listToolsAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#callTool(java.lang.String, java.util.Map)
	 */
	@Override
	public CallToolResult callTool(String name, Map<String, Object> arguments) {
		// TODO Auto-generated method stub
		try {
			return callToolAsync(name, arguments).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#setLoggingLevel(za.co.sindi.ai.mcp.schema.LoggingLevel)
	 */
	@Override
	public void setLoggingLevel(LoggingLevel level) {
		// TODO Auto-generated method stub
		try {
			setLoggingLevelAsync(level).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#complete(za.co.sindi.ai.mcp.schema.Reference, za.co.sindi.ai.mcp.schema.CompleteRequest.Argument)
	 */
	@Override
	public Completion complete(Reference reference, Argument argument) {
		// TODO Auto-generated method stub
		try {
			return completeAsync(reference, argument).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#listTools(java.lang.String)
	 */
	@Override
	public Tool[] listTools(String cursor) {
		// TODO Auto-generated method stub
		try {
			return listToolsAsync(cursor).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#sendRootListChanged()
	 */
	@Override
	public void sendRootListChanged() {
		// TODO Auto-generated method stub
		try {
			sendRootListChangedAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#addRoot(za.co.sindi.ai.mcp.schema.Root)
	 */
	@Override
	public void addRoot(Root root) {
		// TODO Auto-generated method stub
		try {
			addRootAsync(root).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#removeRoot(java.lang.String)
	 */
	@Override
	public void removeRoot(String uri) {
		// TODO Auto-generated method stub
		try {
			removeRootAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
		}
	}
	
	//------------------------------- Async -------------------------------

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#initializeAsync()
	 */
	@Override
	public CompletableFuture<InitializeResult> initializeAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			InitializeRequest request = new InitializeRequest();
			InitializeRequestParameters parameters = new InitializeRequestParameters();
			parameters.setCapabilities(clientCapabilities);
			parameters.setClientInfo(clientInfo);
			parameters.setProtocolVersion(ProtocolVersion.getLatest());
			request.setParameters(parameters);
			
			return sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#pingAsync()
	 */
	@Override
	public CompletableFuture<EmptyResult> pingAsync() {
		// TODO Auto-generated method stub
		CompletableFuture<EmptyResult> cf = new CompletableFuture<>();
		cf.complete(sendRequest(new PingRequest()));
		return cf;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listResourcesAsync()
	 */
	@Override
	public CompletableFuture<Resource[]> listResourcesAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			ListResourcesResult result = sendRequest(new ListResourcesRequest());
			return result.getResources();
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listResourceTemplatesAsync()
	 */
	@Override
	public CompletableFuture<ResourceTemplate[]> listResourceTemplatesAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			ListResourceTemplatesResult result = sendRequest(new ListResourceTemplatesRequest());
			return result.getResourceTemplates();
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#readResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<ResourceContents[]> readResourceAsync(String uri) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			ReadResourceRequest request = new ReadResourceRequest();
			ReadResourceRequestParameters parameters = new ReadResourceRequestParameters();
			parameters.setUri(uri);
			request.setParameters(parameters);
			
			ReadResourceResult result = sendRequest(request);
			return result.getContents();
			
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#subscribeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> subscribeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			SubscribeRequest request = new SubscribeRequest();
			SubscribeRequestParameters parameters = new SubscribeRequestParameters();
			parameters.setUri(uri);
			request.setParameters(parameters);
			
			sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#unsubscribeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> unsubscribeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			UnsubscribeRequest request = new UnsubscribeRequest();
			UnsubscribeRequestParameters parameters = new UnsubscribeRequestParameters();
			parameters.setUri(uri);
			request.setParameters(parameters);
			
			sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#getPromptAsync(java.lang.String, java.util.Map)
	 */
	@Override
	public CompletableFuture<GetPromptResult> getPromptAsync(String name, Map<String, String> arguments) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			GetPromptRequest request = new GetPromptRequest();
			GetPromptRequestParameters parameters = new GetPromptRequestParameters();
			parameters.setName(name);
			parameters.setArguments(arguments);
			request.setParameters(parameters);
			
			return sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listToolsAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Tool[]> listToolsAsync(String cursor) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			ListToolsRequest request = new ListToolsRequest();
			PaginatedRequestParameters parameters = new PaginatedRequestParameters();
			parameters.setCursor(cursor);
			request.setParameters(parameters);
			
			ListToolsResult result = sendRequest(request);
			return result.getTools();
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#callToolAsync(java.lang.String, java.util.Map)
	 */
	@Override
	public CompletableFuture<CallToolResult> callToolAsync(String name, Map<String, Object> arguments) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			CallToolRequest request = new CallToolRequest();
			CallToolRequestParameters parameters = new CallToolRequestParameters();
			parameters.setName(name);
			parameters.setArguments(arguments);
			request.setParameters(parameters);
			
			return sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#setLoggingLevelAsync(za.co.sindi.ai.mcp.schema.LoggingLevel)
	 */
	@Override
	public CompletableFuture<Void> setLoggingLevelAsync(LoggingLevel level) {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			SetLevelRequest request = new SetLevelRequest();
			SetLevelRequestParameters parameters = new SetLevelRequestParameters();
			parameters.setLevel(level);
			request.setParameters(parameters);
			
			sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#completeAsync(za.co.sindi.ai.mcp.schema.Reference, za.co.sindi.ai.mcp.schema.CompleteRequest.Argument)
	 */
	@Override
	public CompletableFuture<Completion> completeAsync(Reference reference, Argument argument) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			CompleteRequest request = new CompleteRequest();
			CompleteRequestParameters parameters = new CompleteRequestParameters();
			parameters.setReference(reference);
			parameters.setArgument(argument);
			request.setParameters(parameters);
			
			CompleteResult result = sendRequest(request);
			return result.getCompletion();
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#sendRootListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendRootListChangedAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			if (Boolean.TRUE.equals(clientCapabilities.getRoots().getListChanged())) {
				sendNotification(new RootsListChangedNotification());
			}
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#addRootAsync(za.co.sindi.ai.mcp.schema.Root)
	 */
	@Override
	public CompletableFuture<Void> addRootAsync(Root root) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(root != null, "Root must not be null");
		Preconditions.checkState(clientCapabilities.getRoots() != null, "Client must be configured with roots capabilities");
		Preconditions.checkState(!roots.containsKey(root.getUri()), "Root with uri '" + root.getUri() + "' already exists");
		
		return CompletableFuture.runAsync(() -> {
			roots.put(root.getUri(), root);
			LOGGER.info("Added root: " + root.getUri() + " (" + root.getName() + ")");
		}).thenCompose(result -> clientCapabilities.getRoots().getListChanged() ? sendRootListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#removeRootAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeRootAsync(String uri) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uri), "Root uri must not be null");
		Preconditions.checkState(clientCapabilities.getRoots() != null, "Client must be configured with roots capabilities");
		Preconditions.checkState(roots.containsKey(uri), "Root with uri '" + uri + "' does not exist");
		
		return CompletableFuture.supplyAsync(() -> {
			Root removed = roots.remove(uri);
			if (removed != null) {
				LOGGER.info("Removed root: " + removed.getUri() + " (" + removed.getName() + ")");
			}
			return removed;
		}).thenCompose(removed -> removed != null && clientCapabilities.getRoots().getListChanged() ? sendRootListChangedAsync() : CompletableFuture.completedFuture(null));
	}
}
