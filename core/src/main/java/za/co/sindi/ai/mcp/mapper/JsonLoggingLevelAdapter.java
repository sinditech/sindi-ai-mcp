/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.LoggingLevel;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonLoggingLevelAdapter implements JsonbAdapter<LoggingLevel, String> {

	@Override
	public String adaptToJson(LoggingLevel format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public LoggingLevel adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return LoggingLevel.of(value);
	}
}
