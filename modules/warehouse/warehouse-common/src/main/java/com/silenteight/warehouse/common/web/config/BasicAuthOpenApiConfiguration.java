package com.silenteight.warehouse.common.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(security = {
    @SecurityRequirement(name = BasicAuthOpenApiConfiguration.APP_SECURITY_SCHEMA)
})
@SecurityScheme(
    name = BasicAuthOpenApiConfiguration.APP_SECURITY_SCHEMA,
    type = SecuritySchemeType.HTTP,
    scheme = "basic",
    in = SecuritySchemeIn.DEFAULT
)
@Configuration
@Profile("swagger & basic-auth")
class BasicAuthOpenApiConfiguration {

  static final String APP_SECURITY_SCHEMA = "basic-security";
}
