package com.silenteight.sens.webapp.keycloak.freemarker;

import freemarker.template.TemplateExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

import static java.nio.charset.Charset.defaultCharset;

@Configuration
class KeycloakFreemarkerConfiguration {

  @NotNull
  private static freemarker.template.Configuration freemarker() {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(
        freemarker.template.Configuration.VERSION_2_3_29);

    configuration.setDefaultEncoding(defaultCharset().name());
    configuration.setLocale(Locale.US);
    configuration.setInterpolationSyntax(
        freemarker.template.Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setTagSyntax(
        freemarker.template.Configuration.SQUARE_BRACKET_TAG_SYNTAX);
    return configuration;
  }

  @Bean
  KeycloakFreemarkerTemplateParser keycloakConfigTemplateProvider() {
    return new KeycloakFreemarkerTemplateParser(freemarker());
  }
}
