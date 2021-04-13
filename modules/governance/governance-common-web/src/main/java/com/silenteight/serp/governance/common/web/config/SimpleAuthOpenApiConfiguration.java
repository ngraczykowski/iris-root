package com.silenteight.serp.governance.common.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(security = {
    @SecurityRequirement(name = SimpleAuthOpenApiConfiguration.APP_SECURITY_SCHEMA)
})
@SecurityScheme(
    name = SimpleAuthOpenApiConfiguration.APP_SECURITY_SCHEMA,
    type = SecuritySchemeType.HTTP,
    scheme = "basic",
    in = SecuritySchemeIn.DEFAULT
)
@Configuration
@Profile("swagger & basic-auth")
class SimpleAuthOpenApiConfiguration {

  static final String APP_SECURITY_SCHEMA = "basic-security";
}
