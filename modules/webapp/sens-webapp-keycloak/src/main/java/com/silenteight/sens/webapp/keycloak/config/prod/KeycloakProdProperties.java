package com.silenteight.sens.webapp.keycloak.config.prod;

import lombok.Value;

import com.silenteight.sens.webapp.keycloak.configloader.KeycloakImportingPolicyProvider;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigurationKey;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakTemplateConfigValues;
import com.silenteight.sens.webapp.keycloak.freemarker.KeycloakTemplatesConfiguration;

import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigurationKey.*;

@Value
@ConfigurationProperties(prefix = "keycloak")
@ConstructorBinding
public class KeycloakProdProperties implements
    KeycloakTemplatesConfiguration, KeycloakImportingPolicyProvider {

  @NotEmpty
  String configTemplatesDir;

  @NotEmpty
  String configTemplateName;

  @Nullable
  String authServerUrl;

  @NotEmpty
  String user;

  @NotEmpty
  String password;

  @NotNull
  Clients clients;

  @NotEmpty
  Policy importingPolicy;

  @Value
  public static class Clients {

    KeycloakClient frontend;

    KeycloakClient backend;

    KeycloakClient account;

    KeycloakClient reportCli;

    KeycloakClient adminCli;

    KeycloakClient realmManagement;
  }

  KeycloakClient getBackend() {
    return getClients().getBackend();
  }

  KeycloakClient getFrontend() {
    return getClients().getFrontend();
  }

  KeycloakClient getAdminCli() {
    return getClients().getAdminCli();
  }

  KeycloakClient getReportCli() {
    return getClients().getReportCli();
  }

  @Override
  public File getTemplatesDir() {
    return Try.of(() -> ResourceUtils.getFile(getConfigTemplatesDir())).get();
  }

  KeycloakTemplateConfigValues getConfigValues() {
    return new KeycloakTemplateConfigValues(
        ImmutableMap.<KeycloakConfigurationKey, Object>builder()
            .put(BACKEND_SECRET, getBackend().getSecret())
            .put(FRONTEND_SECRET, getFrontend().getSecret())
            .put(FRONTEND_BASE_URL, getFrontend().getBaseUrl())
            .put(FRONTEND_ROOT_URL, getFrontend().getRootUrl())
            .put(FRONTEND_REDIRECT_URLS, getFrontend().getRedirectUris())
            .put(REPORT_CLI_SECRET, getReportCli().getSecret())
            .put(CLI_SECRET, getAdminCli().getSecret())
            .build()
    );
  }

  @Value
  @ConstructorBinding
  public static class KeycloakClient {

    @NotEmpty
    String secret;
    @Nullable
    String rootUrl;
    @Nullable
    String baseUrl;

    List<String> redirectUris;
  }
}
