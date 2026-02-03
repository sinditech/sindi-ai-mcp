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
import za.co.sindi.ai.mcp.schema.RequestId;

/**
 * @author Buhake Sindi
 * @since 12 April 2025
 */
public class JsonRequestIdSerialization implements JsonbSerializer<RequestId>, JsonbDeserializer<RequestId> {

//	private static final String JSON_ELEMENT_ID = "id";
	
	@Override
	public RequestId deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
		// TODO Auto-generated method stub
		RequestId id = null;
		try {
			if (parser.isIntegralNumber()) {
				id = RequestId.of(parser.getLong());
			}
		} catch (IllegalStateException e) {
			id = RequestId.of(parser.getString());
		}
		
		return id;
	}

	@Override
	public void serialize(RequestId id, JsonGenerator generator, SerializationContext ctx) {
		// TODO Auto-generated method stub
		if (id != null) {
			if (String.class.equals(id.getType()))
				generator.write((String)id.getValue());
			else if (int.class.equals(id.getType()))
				generator.write((int)id.getValue());
			else if (long.class.equals(id.getType()))
				generator.write((long)id.getValue());
		}
	}
}
