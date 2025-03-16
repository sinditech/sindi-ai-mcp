package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListRootsResult implements ClientResult {
	
	@JsonbProperty
	private Root[] roots;

	/**
	 * @return the roots
	 */
	public Root[] getRoots() {
		return roots;
	}

	/**
	 * @param roots the roots to set
	 */
	public void setRoots(Root[] roots) {
		this.roots = roots;
	}
}
