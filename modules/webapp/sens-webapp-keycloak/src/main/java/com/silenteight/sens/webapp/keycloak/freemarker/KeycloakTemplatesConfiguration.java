package com.silenteight.sens.webapp.keycloak.freemarker;

import lombok.Value;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.File;

@Value
@ConstructorBinding
public class KeycloakTemplatesConfiguration {

  File templatesDir;
}
