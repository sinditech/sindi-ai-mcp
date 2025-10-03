package za.co.sindi.ai.mcp.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Logger;

import za.co.sindi.ai.mcp.schema.CallToolRequest;
import za.co.sindi.ai.mcp.schema.CallToolRequest.CallToolRequestParameters;
import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.CompleteRequest;
import za.co.sindi.ai.mcp.schema.CompleteRequest.Argument;
import za.co.sindi.ai.mcp.schema.CompleteRequest.CompleteRequestParameters;
import za.co.sindi.ai.mcp.schema.CompleteResult;
import za.co.sindi.ai.mcp.schema.CompleteResult.Completion;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest;
import za.co.sindi.ai.mcp.schema.CreateMessageResult;
import za.co.sindi.ai.mcp.schema.Cursor;
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
import za.co.sindi.ai.mcp.schema.MCPSchema;
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
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.RequestHandler;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 21 February 2025
 */
public class DefaultMCPClient implements MCPAsyncClient, MCPClient {
	
	private static final Logger LOGGER = Logger.getLogger(DefaultMCPClient.class.getName());
	
	private final ConcurrentHashMap<String, Root> roots = new ConcurrentHashMap<>();
	
	private final Client client;
	
	private Function<CreateMessageRequest, RequestHandler<CreateMessageResult>> samplingHandler;

	/**
	 * @param transport
	 * @param clientCapabilities
	 * @param clientInfo
	 */
	public DefaultMCPClient(ClientTransport transport, ClientCapabilities clientCapabilities, Implementation clientInfo) {
		this(new DefaultClient(transport, clientCapabilities, clientInfo), null);
	}
	
	/**
	 * @param client
	 * @param samplingHandler
	 */
	public DefaultMCPClient(Client client, Function<CreateMessageRequest, RequestHandler<CreateMessageResult>> samplingHandler) {
		super();
		this.client = client;
		
		if (client.getCapabilities().getRoots() != null) {
			client.addRequestHandler(ListRootsRequest.METHOD_ROOTS_LIST, listRootsRequestHandler());
			client.addNotificationHandler(RootsListChangedNotification.METHOD_NOTIFICATION_ROOTS_lIST_CHANGED, notification -> {});
		}
		
		setSamplingHandler(samplingHandler);
	}

	private RequestHandler<ListRootsResult> listRootsRequestHandler() {
		
		return (request, extra) -> {
			ListRootsResult result = new ListRootsResult();
			result.setRoots(roots.values().toArray(new Root[roots.size()]));
			return result;
		};
	}
	
	// --------------------------
	// Sampling
	// --------------------------
	private RequestHandler<CreateMessageResult> createMessageRequestHandler() {
		
		return (request, extra) -> {
			RequestHandler<CreateMessageResult> handler = samplingHandler.apply(MCPSchema.toRequest(request));
			return handler.handle(request, extra);
		};
	}

	/**
	 * @param samplingHandler the samplingHandler to set
	 */
	public void setSamplingHandler(Function<CreateMessageRequest, RequestHandler<CreateMessageResult>> samplingHandler) {
		if (client.getCapabilities().getSampling() != null) {
			if (samplingHandler == null) {
				throw new IllegalArgumentException("Sampling handler must not be null when client capabilities include sampling");
			}
			this.samplingHandler = samplingHandler;
			client.addRequestHandler(CreateMessageRequest.METHOD_SAMPLING_CREATE_MESSAGE, createMessageRequestHandler());
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Protocol#connect()
	 */
	public void connect() {
		// TODO Auto-generated method stub
		try {
			connectAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			client.onError(e);
		}
	}

	@Override
	public CompletableFuture<Void> connectAsync() {
		// TODO Auto-generated method stub
		return client.connect()
		  .thenCompose(result -> initializeAsync(client.getCapabilities(), client.getClientInfo()))
		  .thenCompose(result -> {
				Preconditions.checkState(result != null, "Server sent invalid initialize result.");
				
				LOGGER.info("Server protocol version: " + result.getProtocolVersion());
				
				client.serverCapabilities = result.getCapabilities();
				client.serverInfo = result.getServerInfo();
				// HTTP transports must set the protocol version in each header after initialization.
				client.getTransport().setProtocolVersion(result.getProtocolVersion());
				client.instructions = result.getInstructions();
				
				//Notify
				return client.sendNotification(new InitializedNotification());
			});
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		client.close();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#initialize(za.co.sindi.ai.mcp.schema.ClientCapabilities, za.co.sindi.ai.mcp.schema.Implementation)
	 */
	@Override
	public InitializeResult initialize(ClientCapabilities clientCapabilities, Implementation clientInfo) {
		// TODO Auto-generated method stub
		try {
			return initializeAsync(clientCapabilities, clientInfo).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			client.onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#ping()
	 */
	@Override
	public void ping() {
		// TODO Auto-generated method stub
		try {
			pingAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
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
			client.onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPClient#listTools(java.lang.String)
	 */
	@Override
	public Tool[] listTools(Cursor cursor) {
		// TODO Auto-generated method stub
		try {
			return listToolsAsync(cursor).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			client.onError(e);
			return null;
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
			client.onError(e);
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
			client.onError(e);
		}
	}
	
	//------------------------------- Async -------------------------------

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#initializeAsync(za.co.sindi.ai.mcp.schema.ClientCapabilities, za.co.sindi.ai.mcp.schema.Implementation)
	 */
	@Override
	public CompletableFuture<InitializeResult> initializeAsync(ClientCapabilities clientCapabilities, Implementation clientInfo) {
		// TODO Auto-generated method stub
		InitializeRequest request = new InitializeRequest();
		InitializeRequestParameters parameters = new InitializeRequestParameters();
		parameters.setCapabilities(clientCapabilities);
		parameters.setClientInfo(clientInfo);
		parameters.setProtocolVersion(ProtocolVersion.getLatest());
		request.setParameters(parameters);
		
		return client.sendRequest(request, InitializeResult.class);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#pingAsync()
	 */
	@Override
	public CompletableFuture<Void> pingAsync() {
		// TODO Auto-generated method stub
		return client.sendRequest(new PingRequest(), EmptyResult.class).thenAccept(result -> {});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listResourcesAsync()
	 */
	@Override
	public CompletableFuture<Resource[]> listResourcesAsync() {
		// TODO Auto-generated method stub
		return client.sendRequest(new ListResourcesRequest(), ListResourcesResult.class).thenApply(result -> result.getResources());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listResourceTemplatesAsync()
	 */
	@Override
	public CompletableFuture<ResourceTemplate[]> listResourceTemplatesAsync() {
		// TODO Auto-generated method stub
		return client.sendRequest(new ListResourceTemplatesRequest(), ListResourceTemplatesResult.class).thenApply(result -> result.getResourceTemplates());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#readResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<ResourceContents[]> readResourceAsync(String uri) {
		// TODO Auto-generated method stub
		ReadResourceRequest request = new ReadResourceRequest();
		ReadResourceRequestParameters parameters = new ReadResourceRequestParameters();
		parameters.setUri(uri);
		request.setParameters(parameters);
		
		return client.sendRequest(request, ReadResourceResult.class).thenApply(result -> result.getContents());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#subscribeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> subscribeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		SubscribeRequest request = new SubscribeRequest();
		SubscribeRequestParameters parameters = new SubscribeRequestParameters();
		parameters.setUri(uri);
		request.setParameters(parameters);
		
		return client.sendRequest(request, EmptyResult.class).thenAccept(result -> {});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#unsubscribeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> unsubscribeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		UnsubscribeRequest request = new UnsubscribeRequest();
		UnsubscribeRequestParameters parameters = new UnsubscribeRequestParameters();
		parameters.setUri(uri);
		request.setParameters(parameters);
		
		return client.sendRequest(request, EmptyResult.class).thenAccept(result -> {});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#getPromptAsync(java.lang.String, java.util.Map)
	 */
	@Override
	public CompletableFuture<GetPromptResult> getPromptAsync(String name, Map<String, String> arguments) {
		// TODO Auto-generated method stub
		GetPromptRequest request = new GetPromptRequest();
		GetPromptRequestParameters parameters = new GetPromptRequestParameters();
		parameters.setName(name);
		parameters.setArguments(arguments);
		request.setParameters(parameters);
		
		return client.sendRequest(request, GetPromptResult.class);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#listToolsAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Tool[]> listToolsAsync(Cursor cursor) {
		// TODO Auto-generated method stub
		ListToolsRequest request = new ListToolsRequest();
		PaginatedRequestParameters parameters = new PaginatedRequestParameters();
		parameters.setCursor(cursor);
		request.setParameters(parameters);
		
		return client.sendRequest(request, ListToolsResult.class).thenApply(result -> result.getTools());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#callToolAsync(java.lang.String, java.util.Map)
	 */
	@Override
	public CompletableFuture<CallToolResult> callToolAsync(String name, Map<String, Object> arguments) {
		// TODO Auto-generated method stub
		CallToolRequest request = new CallToolRequest();
		CallToolRequestParameters parameters = new CallToolRequestParameters();
		parameters.setName(name);
		parameters.setArguments(arguments);
		request.setParameters(parameters);
		
		return client.sendRequest(request, CallToolResult.class);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#setLoggingLevelAsync(za.co.sindi.ai.mcp.schema.LoggingLevel)
	 */
	@Override
	public CompletableFuture<Void> setLoggingLevelAsync(LoggingLevel level) {
		// TODO Auto-generated method stub
		SetLevelRequest request = new SetLevelRequest();
		SetLevelRequestParameters parameters = new SetLevelRequestParameters();
		parameters.setLevel(level);
		request.setParameters(parameters);
		
		return client.sendRequest(request, EmptyResult.class).thenAccept(result -> {});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#completeAsync(za.co.sindi.ai.mcp.schema.Reference, za.co.sindi.ai.mcp.schema.CompleteRequest.Argument)
	 */
	@Override
	public CompletableFuture<Completion> completeAsync(Reference reference, Argument argument) {
		// TODO Auto-generated method stub
		CompleteRequest request = new CompleteRequest();
		CompleteRequestParameters parameters = new CompleteRequestParameters();
		parameters.setReference(reference);
		parameters.setArgument(argument);
		request.setParameters(parameters);
		
		return client.sendRequest(request, CompleteResult.class).thenApply(result -> result.getCompletion());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#addRootAsync(za.co.sindi.ai.mcp.schema.Root)
	 */
	@Override
	public CompletableFuture<Void> addRootAsync(Root root) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(root != null, "Root must not be null");
		Preconditions.checkState(client.getCapabilities().getRoots() != null, "Client must be configured with roots capabilities");
		Preconditions.checkState(!roots.containsKey(root.getUri()), "Root with uri '" + root.getUri() + "' already exists.");
		
		if (client.getCapabilities().getRoots() != null) {
			roots.put(root.getUri(), root);
			LOGGER.info("Added root: " + root.getUri() + " (" + root.getName() + ")");
		}
		
		return client.sendRootListChangedAsync();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.client.MCPAsyncClient#removeRootAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeRootAsync(String uri) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uri), "Root uri must not be null");
		Preconditions.checkState(client.getCapabilities().getRoots() != null, "Client must be configured with roots capabilities");
		Preconditions.checkState(roots.containsKey(uri), "Root with uri '" + uri + "' does not exist.");
		
		if (client.getCapabilities().getRoots() != null) {
			Root removed = roots.remove(uri);
			if (removed != null) {
				LOGGER.info("Removed root: " + removed.getUri() + " (" + removed.getName() + ")");
				return client.sendRootListChangedAsync();
			}
		}
		
		return CompletableFuture.completedFuture(null);
	}
}
