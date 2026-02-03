/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class Icons implements BaseIcons {

	@JsonbProperty
	private Icon[] icons;

	/**
	 * @return the icons
	 */
	public Icon[] getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(Icon[] icons) {
		this.icons = icons;
	}
}
