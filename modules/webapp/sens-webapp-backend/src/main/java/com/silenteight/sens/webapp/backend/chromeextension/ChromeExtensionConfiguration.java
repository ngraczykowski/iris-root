package com.silenteight.sens.webapp.backend.chromeextension;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChromeExtensionConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "chrome-extension")
  ChromeExtensionProperties chromeExtensionProperties() {
    return new ChromeExtensionProperties();
  }

  @Bean
  GetChromeExtensionConfigurationUseCase getChromeExtensionConfigurationUseCase(
      ChromeExtensionProperties properties) {

    return new GetChromeExtensionConfigurationUseCase(properties);
  }
}
