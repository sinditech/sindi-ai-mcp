package za.co.sindi.ai.mcp.mapper;

import java.io.InputStream;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

/**
 * @author Buhake Sindi
 * @since 16 February 2025
 */
public class JSONObjectMapper implements ObjectMapper {
	
	private JsonbConfig newJsonbConfig() {
		JsonbConfig config = new JsonbConfig();
		config.withAdapters(new JsonJSONRPCVersionAdapter(),
							new JsonLoggingLevelAdapter(),
							new JsonRoleAdapter(),
							new JsonProtocolVersionAdapter());
		return config;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.mapper.ObjectMapper#map(java.lang.Object)
	 */
	@Override
	public <E> String map(E object) {
		// TODO Auto-generated method stub
		return JsonbBuilder.create(newJsonbConfig()).toJson(object);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.mapper.ObjectMapper#map(java.lang.String, java.lang.Class)
	 */
	@Override
	public <E> E map(String data, Class<E> type) {
		// TODO Auto-generated method stub
		return JsonbBuilder.create(newJsonbConfig()).fromJson(data, type);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.mapper.ObjectMapper#map(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public <E> E map(InputStream stream, Class<E> type) {
		// TODO Auto-generated method stub
		return JsonbBuilder.create(newJsonbConfig()).fromJson(stream, type);
	}
}
