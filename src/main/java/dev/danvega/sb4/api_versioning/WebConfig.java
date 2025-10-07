package dev.danvega.sb4.api_versioning;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ApiVersionParser;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .usePathSegment(1) // /api/v1/users
                .addSupportedVersions("1.0","2.0")
                .setDefaultVersion("1.0")
                //.useRequestHeader("X-API-Version")
                //.useQueryParam("version")
                //.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .setVersionParser(new SimpleApiVersionParser());
    }
}