/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
public final class ProgressToken implements Serializable {

	private final Class<?> type;
	private final Object value;
	
	/**
	 * @param type
	 * @param value
	 */
	private ProgressToken(Class<?> type, Object value) {
		super();
		this.type = type;
		this.value = Objects.requireNonNull(value, "A non-null value is required.");
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgressToken other = (ProgressToken) obj;
		return Objects.equals(type, other.type) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static ProgressToken of(final String value) {
		return new ProgressToken(String.class, value);
	}
	
	public static ProgressToken of(final int value) {
		return new ProgressToken(int.class, value);
	}
	
	public static ProgressToken of(final long value) {
		return new ProgressToken(int.class, value);
	}
}
