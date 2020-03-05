package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToParseConfigException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import java.io.IOException;

@RequiredArgsConstructor
public class KeycloakConfigParser {

  private final ObjectMapper objectMapper;

  @NotNull
  Try<KeycloakRealmConfig> processJson(String json) {
    return Try.of(() -> createRealmConfig(json))
        .recoverWith(IOException.class, FailedToParseConfigException::from);
  }

  private KeycloakRealmConfig createRealmConfig(String json) throws IOException {
    return new KeycloakRealmConfig(
        processJson(json, RealmRepresentation.class),
        processJson(json, PartialImportRepresentation.class)
    );
  }

  private <T> T processJson(String json, Class<T> type) throws IOException {
    return objectMapper.readValue(json, type);
  }
}
