package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import lombok.ToString;

import com.silenteight.sens.webapp.keycloak.configmigration.MigrationPolicy;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.TemplateFilenameChecker;

import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;

import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

@ToString
class MigrationFile {

  MigrationFile(Resource resource, MigrationFileProperties properties) {
    this.separator = properties.getMigrationsNameSeparator();
    this.charset = properties.getCharset();
    this.templateFilenameChecker = properties.getTemplateFilenameChecker();
    this.resource = resource;
  }

  private final String separator;
  private final Charset charset;
  @ToString.Exclude
  private final TemplateFilenameChecker templateFilenameChecker;
  @ToString.Exclude
  private final Resource resource;

  long getOrder() {
    return parseLong(getOrdinalPart());
  }

  private String getOrdinalPart() {
    return getFilename().split(separator)[0];
  }

  MigrationPolicy getPolicy() {
    return MigrationPolicy.fromName(getPolicyPart());
  }

  private String getPolicyPart() {
    return getFilename().split(separator)[1];
  }

  Try<String> readToString() {
    return Try.of(() -> IOUtils.toString(resource.getInputStream(), charset));
  }

  boolean isTemplate() {
    return templateFilenameChecker.isTemplateFile(getFilename());
  }

  @NotNull
  @ToString.Include
  String getFilename() {
    return requireNonNull(resource.getFilename());
  }
}
