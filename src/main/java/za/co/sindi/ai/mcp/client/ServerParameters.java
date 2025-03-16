package za.co.sindi.ai.mcp.client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 16 February 2025
 */
public class ServerParameters implements Serializable {

	@JsonbProperty
	private String command;

	@JsonbProperty("args")
	private List<String> arguments;

	@JsonbProperty("env")
	private Map<String, String> environmentVariables;

	/**
	 * @param command
	 * @param arguments
	 * @param environmentVariables
	 */
	public ServerParameters(String command, List<String> arguments, Map<String, String> environmentVariables) {
		super();
		this.command = command;
		this.arguments = arguments;
		this.environmentVariables = environmentVariables;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		return arguments;
	}

	/**
	 * @return the environmentVariables
	 */
	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}
}
