package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Implementation implements Serializable {

	@JsonbProperty
	private String name;
	
	@JsonbProperty
	private String version;

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
		this.name = name;
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
}
