/**
 * 
 */
package za.co.sindi.ai.mcp.server;

import java.util.concurrent.Future;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;

/**
 * @author Buhake Sindi
 * @since 26 May 2025
 */
public interface EventStore {

	public Future<EventId> storeEvent(final StreamId streamId, final JSONRPCMessage message);
	
	public Future<StreamId> replayEventAfter(final EventId lastEventId, final Sender sender);
	
	@FunctionalInterface
	public static interface Sender {
		
		public Future<Void> send(final EventId eventId, final JSONRPCMessage message);
	}
}
