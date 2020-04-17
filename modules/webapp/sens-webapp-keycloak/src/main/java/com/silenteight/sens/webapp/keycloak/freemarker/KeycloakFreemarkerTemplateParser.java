package com.silenteight.sens.webapp.keycloak.freemarker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakConfigTemplateParser;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;

@RequiredArgsConstructor
@Slf4j
public class KeycloakFreemarkerTemplateParser implements KeycloakConfigTemplateParser {

  private static final String TEMPLATE_NAME = "";

  private final Configuration freemarker;

  @Override
  public String parse(String template, KeycloakTemplateProperties properties) {
    log.debug(KEYCLOAK_MIGRATION, "Parsing template");
    return Try.of(() -> new Template(TEMPLATE_NAME, new StringReader(template), freemarker))
        .flatMap(t -> Try.of(() -> fillTemplate(t, properties)))
        .onSuccess(unused -> log.debug("Template parsed successfully"))
        .onFailure(reason -> log.error("Could not parse a template", reason))
        .getOrElseThrow(CouldNotProcessTemplateException::new);
  }

  private static String fillTemplate(Template template, KeycloakTemplateProperties properties)
      throws IOException, TemplateException {
    StringWriter stringWriter = new StringWriter();
    template.process(properties.values().orElse(null), stringWriter);

    return stringWriter.toString();
  }
}
