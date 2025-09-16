/**
 * 
 */
package za.co.sindi.ai.mcp.server;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;

/**
 * Simple in-memory implementation of the EventStore interface for resumability
 * This is primarily intended for examples and testing, not for production use
 * where a persistent storage solution would be more appropriate.
 * 
 * @author Buhake Sindi
 * @since 14 September 2025
 */
public class InMemoryEventStore implements EventStore {
	
	private final Map<EventId, StreamMessage> events = Collections.synchronizedSortedMap(new TreeMap<>((o1, o2) -> Collator.getInstance().compare(o1.toString(), o2.toString())));

	@Override
	public CompletableFuture<EventId> storeEvent(StreamId streamId, JSONRPCMessage message) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			final EventId eventId = generateEventId(streamId);
			synchronized (events) {
				events.put(eventId, new StreamMessage(streamId, message));
			}
			return eventId;
		});
	}

	@Override
	public CompletableFuture<StreamId> replayEventAfter(EventId lastEventId, Sender sender) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			if (lastEventId == null || !events.containsKey(lastEventId)) return null;
			
			StreamId streamId = getStreamIdFromEventId(lastEventId);
			if (streamId == null) return null;
			
			final AtomicBoolean foundLastEvent = new AtomicBoolean(false);
			synchronized (events) {
				List<Void> futures = events.entrySet().stream().filter(entry -> entry.getValue().streamId().equals(streamId)).map(entry -> {
					if (entry.getKey().equals(lastEventId)) {
						foundLastEvent.set(true);
					} else {
						if (foundLastEvent.get()) {
							return sender.send(entry.getKey(), entry.getValue().message);
						}
					}
					return null;
				}).map(Objects::requireNonNull).map(CompletableFuture::join).collect(Collectors.toList());
				
			}
			
			return streamId;
		});
	}
	
	private EventId generateEventId(final StreamId streamId) {
		final String eventId = streamId.toString() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
		return EventId.of(eventId);
	}
	
	private StreamId getStreamIdFromEventId(EventId eventId) {
	    final String[] parts = eventId.toString().split("_");
	    return parts.length > 0 ? StreamId.of(parts[0]) : null;
	  }

	private static record StreamMessage(StreamId streamId, JSONRPCMessage message) {}
}
