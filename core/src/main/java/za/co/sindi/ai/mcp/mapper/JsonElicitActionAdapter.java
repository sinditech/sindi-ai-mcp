/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.ElicitResult.Action;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonElicitActionAdapter implements JsonbAdapter<Action, String> {

	@Override
	public String adaptToJson(Action action) throws Exception {
		// TODO Auto-generated method stub
		if (action == null) return null;
		return action.toString();
	}

	@Override
	public Action adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return Action.of(value);
	}
}
