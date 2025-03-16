package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class BlobResourceContents extends ResourceContents {

	@JsonbProperty
	private String blob;

	/**
	 * @return the blob
	 */
	public String getBlob() {
		return blob;
	}

	/**
	 * @param blob the blob to set
	 */
	public void setBlob(String blob) {
		this.blob = blob;
	}
}
