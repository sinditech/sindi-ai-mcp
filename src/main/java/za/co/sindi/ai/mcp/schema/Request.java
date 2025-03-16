package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public interface Request extends Serializable {
	
	default String getMethod() {
		return "";
	};
}
