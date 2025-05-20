package za.co.sindi.ai.mcp.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Level;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.TransportException;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 17 February 2025
 */
public class StdioClientTransport extends AbstractTransport implements ClientTransport {
	
	private final StdioServerParameters serverParameters;
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private Process process;
	
	private CompletableFuture<Void> transportFuture;
	
	/**
	 * @param serverParameters
	 */
	public StdioClientTransport(final StdioServerParameters serverParameters) {
		super();
		this.serverParameters = Objects.requireNonNull(serverParameters, "A server parameters is required.");
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#startAsync()
	 */
	@Override
	public CompletableFuture<Void> startAsync() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio client transport has not been started.");
		}
		
		final List<String> command = new ArrayList<>();
		command.add(serverParameters.getCommand());
		
		if (serverParameters.getArguments() != null) {
			command.addAll(serverParameters.getArguments());
		}

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		if (serverParameters.getEnvironmentVariables() != null)	processBuilder.environment().putAll(serverParameters.getEnvironmentVariables());
		processBuilder.environment().putAll(System.getenv());
		
		if (!Strings.isNullOrEmpty(serverParameters.getCurrentWorkingDirectory())) {
			processBuilder.directory(new File(serverParameters.getCurrentWorkingDirectory()));
		}
		
		transportFuture = new CompletableFuture<>();
		
		// Start the process
		try {
			process = processBuilder.start();
			process.onExit().thenRun(() -> {
				LOGGER.log(Level.INFO, "Subprocess has exited with code: {}", process.exitValue());
            });
		}
		catch (IOException e) {
			Throwable exception = new TransportException("Failed to start process with command: " + command, e);
			transportFuture.completeExceptionally(exception);
			if (getMessageHandler() != null) getMessageHandler().onError(exception);
		}
		
		// Validate process streams
		if (this.process.getInputStream() == null || process.getOutputStream() == null) {
			this.process.destroy();
			throw new TransportException("Process input or output stream is null");
		}
		
		Runnable stdOutReader = () -> {
			String message = readContent(process.inputReader()).get();
			if (message == null) return ;
			if (!message.endsWith("\n")) 
            	throw new TransportException("Message does not end with a newline.");
			
			getMessageHandler().onMessage(MCPSchema.deserializeJSONRPCMessage(message));
		};
		
		Runnable stdioErrReader = () -> {
			String content = readContent(process.inputReader()).get();
			if (content != null && getMessageHandler() != null) {
				getMessageHandler().onError(new IllegalStateException(content));
			}
		};
		
		// Launch both readers on your executor;
        CompletableFuture<Void> outFuture = CompletableFuture.runAsync(stdOutReader, getExecutor());
        CompletableFuture<Void> errFuture = CompletableFuture.runAsync(stdioErrReader, getExecutor());
        
//        transportFuture = outFuture.thenCombine(errFuture, (result, error) -> result)
//	 			.whenComplete((result, throwable) -> {
//						if (throwable != null) {
//							getMessageHandler().onError(throwable);
//						}
//					});
		
		return transportFuture;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		initialized.compareAndSet(true, false);
		process.destroy();
		if (transportFuture != null) transportFuture.cancel(true);
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
				process.getOutputStream().write(content.getBytes(StandardCharsets.UTF_8));
				process.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				throw new TransportException(e);
				if (getMessageHandler() != null) {
					getMessageHandler().onError(e);
				}
			}
		}, getExecutor());
		
		return future; //future.thenCompose(c -> resultMessageFuture.thenCombine(errorMessageFuture, (result, error) -> result));
	}
	
	private <T> Supplier<String> readContent(final BufferedReader reader) {
		return () -> {
			try {
				String line = null;
	            while (initialized.get() && (line = reader.readLine()) != null);
	            return line;
			} catch (IOException e) {
//				throw new TransportException(e);
				if (getMessageHandler() != null) {
					getMessageHandler().onError(e);
				}
				return null;
			}
		};
	}
}
