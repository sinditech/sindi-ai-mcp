/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonProtocolVersionAdapter implements JsonbAdapter<ProtocolVersion, String> {

	@Override
	public String adaptToJson(ProtocolVersion format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public ProtocolVersion adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return ProtocolVersion.of(value);
	}
}
