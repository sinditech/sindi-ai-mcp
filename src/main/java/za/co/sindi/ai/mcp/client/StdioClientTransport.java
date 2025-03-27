package za.co.sindi.ai.mcp.client;

import java.io.BufferedReader;
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

/**
 * @author Buhake Sindi
 * @since 17 February 2025
 */
public class StdioClientTransport extends AbstractTransport implements ClientTransport {
	
	private final ServerParameters serverParameters;
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private Process process;
	
	/**
	 * @param serverParameters
	 */
	public StdioClientTransport(final ServerParameters serverParameters) {
		super();
		this.serverParameters = Objects.requireNonNull(serverParameters, "A server parameters is required.");
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio client transport has not been started.");
		}
		
		final List<String> command = new ArrayList<>();
		command.add(serverParameters.getCommand());
		command.addAll(serverParameters.getArguments());

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		processBuilder.environment().putAll(serverParameters.getEnvironmentVariables());
		processBuilder.environment().putAll(System.getenv());
		
		// Start the process
		try {
			process = processBuilder.start();
			process.onExit().thenRun(() -> {
				LOGGER.log(Level.INFO, "Subprocess has exited with code: {}", process.exitValue());
            });
		}
		catch (IOException e) {
			throw new TransportException("Failed to start process with command: " + command, e);
		}
		
		// Validate process streams
		if (this.process.getInputStream() == null || process.getOutputStream() == null) {
			this.process.destroy();
			throw new TransportException("Process input or output stream is null");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		super.close();
		process.destroy();
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
		});
		CompletableFuture<Void> resultMessageFuture = (getExecutor() == null ? CompletableFuture.supplyAsync(readContent(process.inputReader())) : CompletableFuture.supplyAsync(readContent(process.inputReader()), getExecutor()))
					.thenAccept(line -> {
						if (line == null) return ;
						if (!line.endsWith("\n")) 
		                	throw new TransportException("Message does not end with a newline.");
						
						getMessageHandler().onMessage(MCPSchema.deserializeJSONRPCMessage(line));
					});
		CompletableFuture<Void> errorMessageFuture = (getExecutor() == null ? CompletableFuture.supplyAsync(readContent(process.errorReader())) : CompletableFuture.supplyAsync(readContent(process.errorReader()), getExecutor()))
					.thenAccept(line -> {
						if (line != null && getMessageHandler() != null) {
							getMessageHandler().onError(new IllegalStateException(line));
						}
					});
		
		return future.thenCompose(c -> resultMessageFuture.thenCombine(errorMessageFuture, (result, error) -> result));
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
