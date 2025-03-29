package za.co.sindi.ai.mcp.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import za.co.sindi.commons.net.sse.EventHandler;
import za.co.sindi.commons.net.sse.SSEEventSubscriber;

/**
 * @author Buhake Sindi
 * @since 28 March 2025
 */
public class EventSource implements AutoCloseable {

	private static final Logger LOGGER = Logger.getLogger(EventSource.class.getName());
    
    public enum ReadyState {
        CONNECTING(0),
        OPEN(1),
        CLOSED(2);

        private final int value;
        ReadyState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    // Event listeners
    private final List<Runnable> openListeners = new CopyOnWriteArrayList<>();
    
    // State management
    private volatile ReadyState readyState = ReadyState.CONNECTING;
    private volatile CompletableFuture<Void> connectionFuture;
    
    private final String url;
    private final HttpClient httpClient;
    
    private EventHandler messageEventHandler;
    
    /**
	 * @param url
	 * @param config
	 */
	public EventSource(String url) {
		this(url, HttpClient.newBuilder().build());
	}
    
    /**
	 * @param url
	 * @param httpClient
	 * @param connectionFuture
	 */
	public EventSource(String url, HttpClient httpClient) {
		super();
		this.url = url;
		this.httpClient = httpClient;
		this.connectionFuture = connectionFuture;
	}

	public EventSource onMessage(EventHandler handler) {
    	messageEventHandler = handler;
    	return this;
    }
    
    public EventSource onOpen(Runnable listener) {
    	openListeners.add(listener);
    	return this;
    }
    
    /**
	 * @return the readyState
	 */
	public ReadyState getReadyState() {
		return readyState;
	}

	public CompletableFuture<Void> connect() {
        if (readyState == ReadyState.CLOSED) {
            throw new IllegalStateException("EventSource is closed");
        }
        
        if (connectionFuture != null && !connectionFuture.isDone()) {
            return connectionFuture;
        }
        
        HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Accept", "text/event-stream")
				.header("Cache-Control", "no-cache")
				.GET()
				.build();
        
        connectionFuture = httpClient.sendAsync(request, BodyHandlers.fromLineSubscriber(new SSEEventSubscriber(messageEventHandler)))
                .thenAcceptAsync(response -> {
                	int status = response.statusCode();
        			if (status != 200 && status != 201 && status != 202 && status != 206) {
        				throw new RuntimeException("Failed to connect to SSE stream. Unexpected status code: " + status);
        			}
        			
        			readyState = ReadyState.OPEN;
                    openListeners.forEach(Runnable::run);
                })
                .exceptionally(ex -> {
                	messageEventHandler.onError(ex);
                    return null;
                });

            return connectionFuture;
    }

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		readyState = ReadyState.CLOSED;
        
        // Cancel any pending connection
        if (connectionFuture != null) {
            connectionFuture.cancel(true);
        }

        // Shutdown reconnect executor
//        reconnectExecutor.shutdownNow();

        // Clear all listeners
//        messageListeners.clear();
//        errorListeners.clear();
        openListeners.clear();
	}
}
