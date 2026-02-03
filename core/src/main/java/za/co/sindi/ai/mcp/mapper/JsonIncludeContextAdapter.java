/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest.IncludeContext;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonIncludeContextAdapter implements JsonbAdapter<IncludeContext, String> {

	@Override
	public String adaptToJson(IncludeContext format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public IncludeContext adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return IncludeContext.of(value);
	}
}
