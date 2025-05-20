package za.co.sindi.ai.mcp.client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 16 February 2025
 */
public class StdioServerParameters implements Serializable {

	@JsonbProperty
	private String command;

	@JsonbProperty("args")
	private List<String> arguments;

	@JsonbProperty("env")
	private Map<String, String> environmentVariables;
	
	@JsonbProperty("cwd")
	private String currentWorkingDirectory;
	
	/**
	 * @param command
	 */
	public StdioServerParameters(String command) {
		this(command, null);
	}
	
	/**
	 * @param command
	 * @param arguments
	 */
	public StdioServerParameters(String command, List<String> arguments) {
		this(command, arguments, null);
	}

	/**
	 * @param command
	 * @param arguments
	 * @param environmentVariables
	 */
	public StdioServerParameters(String command, List<String> arguments, Map<String, String> environmentVariables) {
		this(command, arguments, environmentVariables, null);
	}

	/**
	 * @param command
	 * @param arguments
	 * @param environmentVariables
	 * @param currentWorkingDirectory
	 */
	public StdioServerParameters(String command, List<String> arguments, Map<String, String> environmentVariables, String currentWorkingDirectory) {
		super();
		this.command = command;
		this.arguments = arguments;
		this.environmentVariables = environmentVariables;
		this.currentWorkingDirectory = currentWorkingDirectory;
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

	/**
	 * @return the currentWorkingDirectory
	 */
	public String getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}
}
