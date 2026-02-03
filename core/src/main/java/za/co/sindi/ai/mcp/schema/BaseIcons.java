/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public interface BaseIcons extends Serializable {
	
	/**
	 * @return the icons
	 */
	default Icon[] getIcons() {
		return null;
	}
	
	/**
	 * @param icons the icons to set
	 */
	default void setIcons(Icon[] icons) {}
}
