package com.silenteight.sens.webapp.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "sens.web.translations")
  TranslationProperties translationProperties() {
    return new TranslationProperties();
  }
}
