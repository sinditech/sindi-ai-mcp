/**
 * 
 */
package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;

/**
 * @author Buhake Sindi
 * @since 26 May 2025
 */
public interface EventStore {

	public CompletableFuture<EventId> storeEvent(final StreamId streamId, final JSONRPCMessage message);
	
	public CompletableFuture<StreamId> replayEventAfter(final EventId lastEventId, final Sender sender);
	
	@FunctionalInterface
	public static interface Sender {
		
		public CompletableFuture<Void> send(final EventId eventId, final JSONRPCMessage message);
	}
}
