package com.silenteight.sens.webapp.keycloak.configmigration.loader.template;

import java.util.function.Predicate;

public interface TemplateFilenameChecker extends Predicate<String> {

  boolean isTemplateFile(String templateFilename);

  @Override
  default boolean test(String templateFilename) {
    return isTemplateFile(templateFilename);
  }
}
