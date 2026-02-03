/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 10 September 2025
 */
public final class Cursor implements Serializable {

	private final String value;
	
	/**
	 * @param value
	 */
	private Cursor(final String value) {
		super();
		this.value = Objects.requireNonNull(value, "A non-null value is required.");
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cursor other = (Cursor) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return value;
	}

	public static Cursor of(final String value) {
		return new Cursor(value);
	}
}
