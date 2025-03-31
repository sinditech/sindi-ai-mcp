package za.co.sindi.ai.mcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.ai.mcp.shared.TransportException;

/**
 * @author Buhake Sindi
 * @since 17 February 2025
 */
public class StdioServerTransport extends AbstractTransport implements ServerTransport {
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private InputStream inputStream = System.in;
	
	private OutputStream outputStream = System.out;
	
	private CompletableFuture<Void> readerFuture;
	
	/**
	 * 
	 */
	public StdioServerTransport() {
		super();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#startAsync()
	 */
	@Override
	public CompletableFuture<Void> startAsync() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio server transport has not been started.");
		}
		
		Runnable runnable = () -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			try {
				String line = null;
				while (initialized.get() && (line = reader.readLine()) != null);
				if (!line.endsWith("\n")) 
                	throw new TransportException("Message does not end with a newline.");
				
				getMessageHandler().onMessage(MCPSchema.deserializeJSONRPCMessage(line));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				getMessageHandler().onError(e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//Shhh...be quiet! Tonight is the night that we ride....
				}
			}
		};
		
		readerFuture = getExecutor() != null ?  CompletableFuture.runAsync(runnable, getExecutor()) : CompletableFuture.runAsync(runnable);
		return readerFuture;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		initialized.compareAndSet(true, false);
		if (readerFuture != null) readerFuture.cancel(true);
		super.close();
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#sendAsync(za.co.sindi.ai.mcp.schema.JSONRPCMessage)
	 */
	@Override
	public CompletableFuture<Void> sendAsync(JSONRPCMessage message) {
		// TODO Auto-generated method stub
		if (!initialized.get()) {
			throw new TransportException("This transport was not started or closed..");
		}
		
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			try {
				String content = MCPSchema.serializeJSONRPCMessage(message) + "\n"; // getMapper().map(message)
				outputStream.write(content.getBytes(StandardCharsets.UTF_8));
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new TransportException(e);
			}
		});
		
		return future;
	}
}
