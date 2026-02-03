/**
 * 
 */
package za.co.sindi.ai.mcp.shared;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ElicitRequest.ElicitRequestURLParameters;
import za.co.sindi.ai.mcp.schema.ErrorCodes;

/**
 * @author Buhake Sindi
 * @since 12 January 2026
 */
public class URLElicitationRequiredError extends MCPError {
	
	public URLElicitationRequiredError(final ElicitRequestURLParameters[] elicitations) {
		this(elicitations, null);
	}
	
	public URLElicitationRequiredError(final List<ElicitRequestURLParameters> elicitations) {
		this(elicitations, null);
	}

	public URLElicitationRequiredError(final ElicitRequestURLParameters[] elicitations, final Map<String, Object> otherInfos) {
		super(ErrorCodes.URL_ELICITATION_REQUIRED, "Error");
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("elicitations", Objects.requireNonNull(elicitations));
		if (otherInfos != null) {
			data.putAll(otherInfos);
		}
		
		setData(data);
	}
	
	public URLElicitationRequiredError(final List<ElicitRequestURLParameters> elicitations, final Map<String, Object> otherInfos) {
		this(Objects.requireNonNull(elicitations).toArray(new ElicitRequestURLParameters[elicitations.size()]), otherInfos);
	}
}
