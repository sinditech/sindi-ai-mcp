/**
 * 
 */
package za.co.sindi.ai.mcp.server;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 26 May 2025
 */
public class EventId implements Serializable {

	private final String id;

	/**
	 * @param id
	 */
	private EventId(String id) {
		super();
		this.id = id;
	}
	
	public static EventId of(final String id) {
		return new EventId(id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventId other = (EventId) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id;
	}
}
