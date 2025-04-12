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
import za.co.sindi.ai.mcp.shared.ProgressToken;

/**
 * @author Buhake Sindi
 * @since 12 April 2025
 */
public class JsonProgressTokenSerialization implements JsonbSerializer<ProgressToken>, JsonbDeserializer<ProgressToken> {
	
	private static final String JSON_ELEMENT_ID = "progressToken";

	@Override
	public ProgressToken deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
		// TODO Auto-generated method stub
		ProgressToken progressToken = null;
		boolean progressTokenFound = false;
		while (parser.hasNext()) {
			Event event = parser.next();
			if (event == Event.KEY_NAME) {
				progressTokenFound = JSON_ELEMENT_ID.equals(parser.getString());
				continue ;
			}
			if (progressTokenFound && event == Event.VALUE_NUMBER)
				progressToken = ProgressToken.of(parser.getLong());
			else if (progressTokenFound && event == Event.VALUE_STRING)
				progressToken = ProgressToken.of(parser.getString());
		}
		
		//Maybe we didn't find it....
		if (progressToken == null) {
			try {
				if (parser.isIntegralNumber()) {
					progressToken = ProgressToken.of(parser.getLong());
				}
			} catch (IllegalStateException e) {
				progressToken = ProgressToken.of(parser.getString());
			}
		}
		return progressToken;
	}

	@Override
	public void serialize(ProgressToken progressToken, JsonGenerator generator, SerializationContext ctx) {
		// TODO Auto-generated method stub
		if (progressToken != null) {
			if (String.class.equals(progressToken.getType()))
				generator.write((String)progressToken.getValue());
			else if (int.class.equals(progressToken.getType()))
				generator.write((int)progressToken.getValue());
			else if (long.class.equals(progressToken.getType()))
				generator.write((long)progressToken.getValue());
		}
	}
}
