package com.silenteight.sens.webapp.keycloak.freemarker;

import com.silenteight.sens.webapp.keycloak.config.prod.KeycloakProdConfiguration;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

@Configuration
@ConditionalOnBean(KeycloakProdConfiguration.class)
class KeycloakFreemarkerConfiguration {

  @Bean
  freemarker.template.Configuration freemarkerConfiguration(
      KeycloakTemplatesConfiguration keycloakTemplatesConfiguration) throws IOException {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(
        freemarker.template.Configuration.VERSION_2_3_29);

    configuration.setDirectoryForTemplateLoading(keycloakTemplatesConfiguration.getTemplatesDir());
    configuration.setDefaultEncoding(Charset.defaultCharset().name());
    configuration.setLocale(Locale.US);
    configuration.setInterpolationSyntax(
        freemarker.template.Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setTagSyntax(
        freemarker.template.Configuration.SQUARE_BRACKET_TAG_SYNTAX);

    return configuration;
  }

  @Bean
  KeycloakFreemarkerTemplateProvider keycloakConfigTemplateProvider(
      freemarker.template.Configuration freeMarkerConfig) {
    return new KeycloakFreemarkerTemplateProvider(freeMarkerConfig);
  }
}
