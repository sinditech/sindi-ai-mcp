package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Implementation extends BaseMetadata implements BaseIcons {
	
	@JsonbProperty
	private String version;
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private String websiteUrl;
	
	@JsonbProperty
	private Icon[] icons;

	/**
	 * 
	 */
	public Implementation() {
		super();
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param version
	 */
	public Implementation(String name, String version) {
		super();
		setName(name);
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	/**
	 * @param websiteUrl the websiteUrl to set
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	@Override
	public Icon[] getIcons() {
		// TODO Auto-generated method stub
		return icons;
	}

	@Override
	public void setIcons(Icon[] icons) {
		// TODO Auto-generated method stub
		this.icons = icons;
	}
}
