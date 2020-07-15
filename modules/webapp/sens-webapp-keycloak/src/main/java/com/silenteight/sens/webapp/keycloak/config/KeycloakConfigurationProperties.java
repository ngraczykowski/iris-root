package com.silenteight.sens.webapp.keycloak.config;

import lombok.Value;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.KeycloakResourcesMigrationsLoaderProperties;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.TemplateFilenameChecker;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.nio.charset.Charset;
import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;

@ConfigurationProperties(prefix = "keycloak")
@Value
@ConstructorBinding
class KeycloakConfigurationProperties implements
    KeycloakAdminClientFactory,
    KeycloakResourcesMigrationsLoaderProperties {

  @NotNull
  MigratorProperties migrator;

  @NotNull
  AdapterConfig adapter;

  @Override
  public Keycloak getAdminClient() {
    return getMigrator().toAdminClient();
  }

  @Override
  public String getMigrationsNameSeparator() {
    return getMigrator().getMigrationsNameSeparator();
  }

  @Override
  public Charset getCharset() {
    return Charset.defaultCharset();
  }

  @Override
  public String getMigrationsPath() {
    return getMigrator().getMigrations();
  }

  @Override
  public KeycloakTemplateProperties getConfigTemplateProperties() {
    return () -> ofNullable(getMigrator().getTemplateProperties());
  }

  @Override
  public TemplateFilenameChecker getTemplateFilenameChecker() {
    return templateFilename -> templateFilename.endsWith(".ftl");
  }
}
