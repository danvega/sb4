package dev.danvega.sb4.api_versioning;

import org.springframework.web.accept.ApiVersionParser;

public class SimpleApiVersionParser implements ApiVersionParser {

    // allows us to use /api/v2/users instead of /api/2.0/users
    @Override
    public Comparable parseVersion(String version) {
        // Remove "v" prefix if it exists (v1 becomes 1, v2 becomes 2)
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }

        // Add .0 if it's just a single number (1 becomes 1.0, 2 becomes 2.0)
        if (!version.contains(".")) {
            version = version + ".0";
        }

        return version;
    }
}