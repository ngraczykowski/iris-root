package com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;
import com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile.exception.CouldNotReadConfigFileException;

import io.vavr.control.Try;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.apache.commons.io.FileUtils.readFileToString;

@RequiredArgsConstructor
class KeycloakSingleFileConfigProvider implements KeycloakConfigProvider {

  private final File configFile;
  private final Charset charset;

  @Override
  public String json() {
    return Try.of(() -> readFileToString(configFile, charset))
        .recoverWith(IOException.class, CouldNotReadConfigFileException::from)
        .get();
  }
}
