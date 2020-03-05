package com.silenteight.sens.webapp.keycloak.config.dev;

import com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile.KeycloakSingleFileConfigProvider;

import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.charset.Charset;

@Profile("dev")
@EnableConfigurationProperties(KeycloakDevConfigProperties.class)
@Configuration
class KeycloakDevConfiguration {

  @Bean
  KeycloakSingleFileConfigProvider keycloakConfigProvider(
      KeycloakDevConfigProperties keycloakDevProperties) {
    File configFile = new File(keycloakDevProperties.getConfigPath());

    return new KeycloakSingleFileConfigProvider(configFile, Charset.defaultCharset());
  }

  @Bean
  AdapterConfig devAdapterConfig(KeycloakDevConfigProperties keycloakDevConfigProperties) {
    return keycloakDevConfigProperties.getAdapterConfig();
  }
}
