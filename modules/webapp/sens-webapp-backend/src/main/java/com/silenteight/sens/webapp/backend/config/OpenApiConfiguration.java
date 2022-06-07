package com.silenteight.sens.webapp.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.sep.auth.authentication.RestConstants.ACCESS_TOKEN_URL;
import static com.silenteight.sep.auth.authentication.RestConstants.AUTHORIZATION_URL;

@OpenAPIDefinition(
    security = @SecurityRequirement(name = OpenApiConfiguration.WEBAPP_OPENID_SECURITY_SCHEMA))
@SecurityScheme(
    name = OpenApiConfiguration.WEBAPP_OPENID_SECURITY_SCHEMA,
    type = SecuritySchemeType.OAUTH2,
    in = SecuritySchemeIn.HEADER,
    bearerFormat = "jwt",
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(authorizationUrl = AUTHORIZATION_URL, tokenUrl = ACCESS_TOKEN_URL)))
@Configuration
@Profile("swagger")
class OpenApiConfiguration {

  static final String WEBAPP_OPENID_SECURITY_SCHEMA = "oauth2-webapp";
}
