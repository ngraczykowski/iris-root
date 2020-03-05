package com.silenteight.sens.webapp.keycloak.freemarker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigTemplate;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigurationKey;
import com.silenteight.sens.webapp.keycloak.freemarker.exception.CouldNotProcessTemplateException;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.vavr.control.Try;
import one.util.streamex.EntryStream;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class FreemarkerKeycloakConfigTemplate implements KeycloakConfigTemplate {

  private final Template template;

  @Override
  public Try<String> process(Map<KeycloakConfigurationKey, String> values) {
    return Try.of(() -> fillTemplate(values))
        .recoverWith(IOException.class, CouldNotProcessTemplateException::from)
        .recoverWith(TemplateException.class, CouldNotProcessTemplateException::from);
  }

  private String fillTemplate(Map<KeycloakConfigurationKey, String> values)
      throws IOException, TemplateException {
    StringWriter stringWriter = new StringWriter();
    template.process(toStringKeys(values), stringWriter);

    return stringWriter.toString();
  }

  private static Map<String, String> toStringKeys(Map<KeycloakConfigurationKey, String> values) {
    return EntryStream.of(values).mapKeys(Enum::name).toMap();
  }
}
