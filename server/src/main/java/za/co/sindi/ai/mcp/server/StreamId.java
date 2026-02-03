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
public class StreamId implements Serializable {

	private final String id;

	/**
	 * @param id
	 */
	private StreamId(String id) {
		super();
		this.id = id;
	}
	
	public static StreamId of(final String id) {
		return new StreamId(id);
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
		StreamId other = (StreamId) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id;
	}
}
