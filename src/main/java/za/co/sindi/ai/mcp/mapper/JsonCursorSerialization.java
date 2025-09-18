/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import java.lang.reflect.Type;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import za.co.sindi.ai.mcp.schema.Cursor;

/**
 * @author Buhake Sindi
 * @since 19 September 2025
 */
public class JsonCursorSerialization implements JsonbSerializer<Cursor>, JsonbDeserializer<Cursor> {

//	private static final String JSON_ELEMENT_ID = "id";
	
	@Override
	public Cursor deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
		// TODO Auto-generated method stub
		if (parser.currentEvent() == Event.VALUE_NULL) return null;
		return Cursor.of(parser.getString());
	}

	@Override
	public void serialize(Cursor cursor, JsonGenerator generator, SerializationContext ctx) {
		// TODO Auto-generated method stub
		generator.write(cursor != null ? cursor.toString() : null);
	}
}
