package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.ProtocolVersion;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public interface ClientTransport extends Transport {

	default void setProtocolVersion(final ProtocolVersion protocolVersion) {}
}
