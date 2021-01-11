package com.silenteight.serp.governance.common.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(security = {
    @SecurityRequirement(name = OpenApiConfiguration.WEBAPP_OPENID_SECURITY_SCHEMA)
})
@SecurityScheme(
    name = OpenApiConfiguration.WEBAPP_OPENID_SECURITY_SCHEMA,
    type = SecuritySchemeType.OAUTH2,
    in = SecuritySchemeIn.HEADER,
    bearerFormat = "jwt",
    flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(
            authorizationUrl = "${keycloak.adapter.authServerUrl}/realms/${keycloak.adapter.realm}"
                + "/protocol/openid-connect/auth",
            tokenUrl = "${keycloak.adapter.authServerUrl}/realms/${keycloak.adapter.realm}"
                + "/protocol/openid-connect/token"
        )
    )
)
@Configuration
@Profile("swagger")
class OpenApiConfiguration {

  static final String WEBAPP_OPENID_SECURITY_SCHEMA = "oauth2-webapp";
}
