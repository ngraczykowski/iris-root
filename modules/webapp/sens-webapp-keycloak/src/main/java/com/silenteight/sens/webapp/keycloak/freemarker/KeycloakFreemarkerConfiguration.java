package com.silenteight.sens.webapp.keycloak.freemarker;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

@Configuration
class KeycloakFreemarkerConfiguration {

  @Bean
  freemarker.template.Configuration freemarkerConfiguration(
      KeycloakTemplatesConfiguration keycloakTemplatesConfiguration) throws IOException {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(
        freemarker.template.Configuration.VERSION_2_3_29);

    configuration.setDirectoryForTemplateLoading(keycloakTemplatesConfiguration.getTemplatesDir());
    configuration.setDefaultEncoding(Charset.defaultCharset().name());
    configuration.setLocale(Locale.US);
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    return configuration;
  }

  @Bean
  KeycloakFreemarkerTemplateProvider keycloakConfigTemplateProvider(
      freemarker.template.Configuration freeMarkerConfig) {
    return new KeycloakFreemarkerTemplateProvider(freeMarkerConfig);
  }
}
