/**
 * 
 */
package za.co.sindi.ai.mcp.mapper;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.mcp.schema.Role;

/**
 * @author Buhake Sindi
 * @since 24 January 2024
 */
public class JsonRoleAdapter implements JsonbAdapter<Role, String> {

	@Override
	public String adaptToJson(Role format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public Role adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return Role.of(value);
	}
}
