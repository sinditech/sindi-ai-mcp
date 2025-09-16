package za.co.sindi.ai.mcp.server;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters;
import za.co.sindi.ai.mcp.schema.CreateMessageResult;
import za.co.sindi.ai.mcp.schema.ElicitRequest;
import za.co.sindi.ai.mcp.schema.ElicitRequest.ElicitRequestParameters;
import za.co.sindi.ai.mcp.schema.ElicitResult;
import za.co.sindi.ai.mcp.schema.EmptyResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
import za.co.sindi.ai.mcp.schema.ListRootsRequest;
import za.co.sindi.ai.mcp.schema.ListRootsResult;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.PromptListChangedNotification;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.schema.ResourceListChangedNotification;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification.ResourceUpdatedNotificationParameters;
import za.co.sindi.ai.mcp.schema.Root;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.schema.ServerNotification;
import za.co.sindi.ai.mcp.schema.ServerRequest;
import za.co.sindi.ai.mcp.schema.ServerResult;
import za.co.sindi.ai.mcp.schema.ToolListChangedNotification;
import za.co.sindi.ai.mcp.shared.InitializedHandler;
import za.co.sindi.ai.mcp.shared.Protocol;
import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Server extends Protocol<ServerTransport, ServerRequest, ServerNotification, ServerResult> {
	
	private final Implementation serverInfo;
	private final ServerCapabilities capabilities;
	private final String instructions;

	private ClientCapabilities clientCapabilities;
	private Implementation clientVersion;
	protected InitializedHandler initializedHandler;
	
	/**
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	protected Server(final Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		super();
		this.serverInfo = Objects.requireNonNull(serverInfo, "A server info is required.");
		this.capabilities = serverCapabilities; // Objects.requireNonNull(serverCapabilities, "A server capabilities is required.");
		this.instructions = instructions;
		
		addRequestHandler(InitializeRequest.METHOD_INITIALIZE, (request, extra) -> {
			InitializeRequest initializeRequest = MCPSchema.toRequest(request);
			return onInitialize(initializeRequest);
		});
		
		addNotificationHandler(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, notification -> {
			if (initializedHandler != null) initializedHandler.onInInitialized();
		});
	}
	
	public CompletableFuture<Void> ping() {
		// TODO Auto-generated method stub
		return sendRequest(new PingRequest(), EmptyResult.class).thenAccept(result -> {});
	}

	public CompletableFuture<CreateMessageResult> createMessage(CreateMessageRequestParameters parameters) {
		// TODO Auto-generated method stub
		if (clientCapabilities == null || clientCapabilities.getSampling() == null) {
			throw new IllegalStateException("Client does not support sampling (required for " + CreateMessageRequest.METHOD_SAMPLING_CREATE_MESSAGE + ")");
		}
		CreateMessageRequest request = new CreateMessageRequest();
		request.setParameters(parameters);
		return sendRequest(request, CreateMessageResult.class);
	}
	
	public CompletableFuture<ElicitResult> elicitInput(ElicitRequestParameters parameters) {
		if (clientCapabilities == null || clientCapabilities.getElicitation() == null) {
			throw new IllegalStateException("Client does not support elicitation (required for " + ElicitRequest.METHOD_ELICITATION_CREATE + ")");
		}
		ElicitRequest request = new ElicitRequest();
		request.setParameters(parameters);
		return sendRequest(request, ElicitResult.class);
	}

	public CompletableFuture<Root[]> listRoots() {
		// TODO Auto-generated method stub
		if (clientCapabilities == null || clientCapabilities.getRoots() == null) {
			throw new IllegalStateException("Client does not support listing roots. (required for " + ListRootsRequest.METHOD_ROOTS_LIST + ")");
		}
		return sendRequest(new ListRootsRequest(), ListRootsResult.class).thenApply(result -> result.getRoots());
	}
	
	public CompletableFuture<Void> sendLoggingMessage(final LoggingMessageNotificationParameters parameters) {
		String sessionId = null;
		if (getTransport() != null) {
			sessionId = getTransport().getSessionId();
		}
		return sendLoggingMessage(parameters, sessionId);
	}
	
	public CompletableFuture<Void> sendResourceUpdated(final String uri) {
		ResourceUpdatedNotification notification = new ResourceUpdatedNotification();
		ResourceUpdatedNotificationParameters parameters = new ResourceUpdatedNotificationParameters();
		parameters.setUri(uri);
		notification.setParameters(parameters);
		return sendNotification(notification);
	}
	
	public CompletableFuture<Void> sendResourceListChanged() {
		return sendNotification(new ResourceListChangedNotification());
	}
	
	public CompletableFuture<Void> sendToolListChanged() {
		return sendNotification(new ToolListChangedNotification());
	}
	
	public CompletableFuture<Void> sendPromptListChanged() {
		return sendNotification(new PromptListChangedNotification());
	}
	
	protected InitializeResult onInitialize(final InitializeRequest initializeRequest) {
		final ProtocolVersion requestedVersion = initializeRequest.getParameters().getProtocolVersion();
		clientCapabilities = initializeRequest.getParameters().getCapabilities();
		clientVersion = initializeRequest.getParameters().getClientInfo();
		
		final ProtocolVersion protocolVersion = Arrays.stream(ProtocolVersion.values()).anyMatch(pv -> pv == requestedVersion) ? requestedVersion : ProtocolVersion.LATEST_PROTOCOL_VERSION;
		
		InitializeResult result = new InitializeResult();
		result.setCapabilities(capabilities);
		result.setInstructions(instructions);
		result.setServerInfo(serverInfo);
		result.setProtocolVersion(protocolVersion);
		
		return result;
	}
	
	public abstract CompletableFuture<Void> sendLoggingMessage(final LoggingMessageNotificationParameters parameters, final String sessionId);
	
	/**
	 * @return the serverInfo
	 */
	public Implementation getServerInfo() {
		return serverInfo;
	}

	/**
	 * @return the clientCapabilities
	 */
	public ClientCapabilities getClientCapabilities() {
		return clientCapabilities;
	}

	/**
	 * @return the clientVersion
	 */
	public Implementation getClientVersion() {
		return clientVersion;
	}

	/**
	 * @return the capabilities
	 */
	public ServerCapabilities getCapabilities() {
		return capabilities;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param initializedHandler the initializedHandler to set
	 */
	public void setInitializedHandler(InitializedHandler initializedHandler) {
		this.initializedHandler = initializedHandler;
	}
}
