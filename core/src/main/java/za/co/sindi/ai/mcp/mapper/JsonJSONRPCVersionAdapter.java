/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.JSONRPCVersion;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonJSONRPCVersionAdapter implements JsonbAdapter<JSONRPCVersion, String> {

	@Override
	public String adaptToJson(JSONRPCVersion format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public JSONRPCVersion adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return JSONRPCVersion.of(value);
	}
}
