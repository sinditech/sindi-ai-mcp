package za.co.sindi.ai.mcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Logger;

import jakarta.json.bind.JsonbException;
import za.co.sindi.ai.mcp.mapper.JSONObjectMapper;
import za.co.sindi.ai.mcp.mapper.ObjectMapper;
import za.co.sindi.ai.mcp.schema.JSONRPCError;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.JSONRPCResult;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.ai.mcp.shared.TransportException;

/**
 * @author Buhake Sindi
 * @since 17 February 2025
 */
public class StdioServerTransport extends AbstractTransport implements ServerTransport {
	
	private static final Logger LOGGER = Logger.getLogger(StdioServerTransport.class.getName());
	
	private AtomicBoolean initialized = new AtomicBoolean(false);
	
	private InputStream inputStream = System.in;
	
	private OutputStream outputStream = System.out;
	
	/**
	 * 
	 */
	public StdioServerTransport() {
		this(new JSONObjectMapper());
	}

	/**
	 * @param objectMapper
	 */
	public StdioServerTransport(final ObjectMapper objectMapper) {
		super(objectMapper);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio server transport has not been started.");
		}
		
		
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		super.close();
		initialized.compareAndSet(true, false);
		if (getMessageHandler() != null) {
			getMessageHandler().onClose();
		}
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
				String content = getMapper().map(message) + "\n";
				outputStream.write(content.getBytes(StandardCharsets.UTF_8));
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new TransportException(e);
			}
		});
		CompletableFuture<Void> resultMessageFuture = (getExecutor() == null ? CompletableFuture.supplyAsync(readContent(new BufferedReader(new InputStreamReader(inputStream)))) : CompletableFuture.supplyAsync(readContent(new BufferedReader(new InputStreamReader(inputStream))), getExecutor()))
					.thenAccept(line -> {
						if (line == null) return ;
						if (!line.endsWith("\n")) 
		                	throw new TransportException("Message does not end with a newline.");
						
						JSONRPCMessage responseMessage = null;
						try {
							responseMessage = getMapper().map(line, JSONRPCResult.class);
						} catch (JsonbException e) {
							responseMessage = getMapper().map(line, JSONRPCError.class);
						}
						
						if (responseMessage != null) {
							getMessageHandler().onMessage(responseMessage);
						}
					});
		
		return future.thenCompose(c -> resultMessageFuture);
	}
	
	private <T> Supplier<String> readContent(final BufferedReader reader) {
		return () -> {
			try {
				String line = null;
	            while (initialized.get() && (line = reader.readLine()) != null);
	            return line;
			} catch (IOException e) {
				throw new TransportException(e);
			}
		};
	}
}
