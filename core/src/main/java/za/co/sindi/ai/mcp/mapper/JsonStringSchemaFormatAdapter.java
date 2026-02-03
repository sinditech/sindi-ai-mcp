/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.StringSchema.Format;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonStringSchemaFormatAdapter implements JsonbAdapter<Format, String> {

	@Override
	public String adaptToJson(Format format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public Format adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return Format.of(value);
	}
}
