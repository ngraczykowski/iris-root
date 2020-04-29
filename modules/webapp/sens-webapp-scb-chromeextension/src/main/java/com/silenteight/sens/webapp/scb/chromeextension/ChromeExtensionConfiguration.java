package com.silenteight.sens.webapp.scb.chromeextension;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
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
