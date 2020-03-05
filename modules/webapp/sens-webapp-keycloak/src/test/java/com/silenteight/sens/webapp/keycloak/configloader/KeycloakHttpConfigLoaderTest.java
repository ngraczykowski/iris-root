package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToCreateRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToFindRealmException;
import com.silenteight.sens.webapp.keycloak.configloader.exceptions.FailedToPerformPartialImportException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;

import static com.silenteight.sens.webapp.common.support.jackson.JsonConversionHelper.INSTANCE;
import static com.silenteight.sens.webapp.keycloak.configloader.KeycloakHttpConfigLoaderTest.KeycloakHttpConfigLoaderFixtures.CLIENT_ERROR_EXCEPTION;
import static com.silenteight.sens.webapp.keycloak.configloader.KeycloakHttpConfigLoaderTest.KeycloakHttpConfigLoaderFixtures.NOT_FOUND_EXCEPTION;
import static com.silenteight.sens.webapp.keycloak.configloader.RecursiveEqualsMatcher.eqRecursively;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakHttpConfigLoaderTest {

  @Mock
  private Keycloak keycloak;

  @Mock
  private RealmsResource realmsResource;

  ObjectMapper objectMapper = INSTANCE.objectMapper();

  @Mock
  private RealmResource realmResource;

  private KeycloakHttpConfigLoader underTest;

  private KeycloakHttpConfigLoaderFixtures fixtures =
      new KeycloakHttpConfigLoaderFixtures(objectMapper);

  @BeforeEach
  void setUp() {
    willReturn(realmsResource).given(keycloak).realms();
    underTest =
        new KeycloakConfigLoaderConfiguration().keycloakHttpConfigLoader(keycloak, objectMapper);
  }

  @Nested
  class GivenRealmDoesntExist {

    @BeforeEach
    void setUp() {
      willThrow(NOT_FOUND_EXCEPTION).given(realmsResource).realm(fixtures.config.realmName());
    }

    @Test
    void correctConfig_triesToCreateRealmAndFailsAsRealmIsNotFound() {
      Try<Void> actual = underTest.load(fixtures.config);

      then(realmsResource).should().create(eqRecursively(fixtures.config.asRealmRepresentation()));
      assertThat(actual.getCause()).isInstanceOf(FailedToFindRealmException.class);
    }

    @Test
    void correctConfig_failedToFindRealmException() {
      Try<Void> actual = underTest.load(fixtures.config);

      assertThat(actual.getCause()).isInstanceOf(FailedToFindRealmException.class);
    }
  }

  @Nested
  class GivenRealmDoesntExistAndCantCreateNew {

    @BeforeEach
    void setUp() {
      willThrow(NOT_FOUND_EXCEPTION).given(realmsResource).realm(fixtures.config.realmName());
      willThrow(CLIENT_ERROR_EXCEPTION).given(realmsResource).create(any());
    }

    @Test
    void correctConfig_triesToFindAndCreateRealm() {
      underTest.load(fixtures.config);

      then(realmsResource).should().realm(fixtures.config.realmName());
      then(realmsResource).should().create(eqRecursively(fixtures.config.asRealmRepresentation()));
    }

    @Test
    void correctConfig_failedToCreateRealmException() {
      Try<Void> actual = underTest.load(fixtures.config);

      assertThat(actual.getCause()).isInstanceOf(FailedToCreateRealmException.class);
    }
  }

  @Nested
  class GivenCantFindRealmAndCanCreateRealmAndCantPartialImport {

    @BeforeEach
    void setUp() {
      willThrow(NOT_FOUND_EXCEPTION)
          .willReturn(realmResource)
          .given(realmsResource).realm(fixtures.config.realmName());

      willThrow(CLIENT_ERROR_EXCEPTION).given(realmResource).partialImport(any());
    }

    @Test
    void correctConfig_createsAndFindsRealmTwiceAndTriesToPartiallyImport() {
      underTest.load(fixtures.config);

      then(realmsResource).should().create(eqRecursively(fixtures.config.asRealmRepresentation()));
      then(realmsResource).should(times(2)).realm(fixtures.config.realmName());
      then(realmResource)
          .should()
          .partialImport(
              eqRecursively(fixtures.config.asPartialImportRepresentation(Policy.OVERWRITE)));
    }

    @Test
    void correctConfig_returnsCantPerformPartialImportException() {
      Try<Void> actual = underTest.load(fixtures.config);

      assertThat(actual.getCause()).isInstanceOf(FailedToPerformPartialImportException.class);
    }
  }

  @Nested
  class GivenCanFindRealmAndCantPartialImport {

    @BeforeEach
    void setUp() {
      willReturn(realmResource).given(realmsResource).realm(fixtures.config.realmName());
      willThrow(CLIENT_ERROR_EXCEPTION).given(realmResource).partialImport(any());
    }

    @Test
    void correctConfig_triesToPerformPartialImport() {
      underTest.load(fixtures.config);

      then(realmResource)
          .should()
          .partialImport(
              eqRecursively(fixtures.config.asPartialImportRepresentation(Policy.OVERWRITE)));
    }

    @Test
    void correctConfig_failedToPerformPartialImportException() {
      Try<Void> actual = underTest.load(fixtures.config);

      assertThat(actual.getCause()).isInstanceOf(FailedToPerformPartialImportException.class);
    }
  }


  @Nested
  class GivenCanFindRealmAndPerformPartialImport {

    @BeforeEach
    void setUp() {
      willReturn(realmResource).given(realmsResource).realm(fixtures.config.realmName());
    }

    @Test
    void returnsSuccess() {
      Try<Void> actual = underTest.load(fixtures.config);

      assertThat(actual.isSuccess()).isTrue();
    }
  }

  @RequiredArgsConstructor
  static class KeycloakConfig implements KeycloakConfigProvider {

    private final String json;
    private final ObjectMapper objectMapper;

    PartialImportRepresentation asPartialImportRepresentation(Policy policy) {
      PartialImportRepresentation partialImportRepresentation =
          mapTo(PartialImportRepresentation.class);
      partialImportRepresentation.setIfResourceExists(policy.name());
      return partialImportRepresentation;
    }

    private <T> T mapTo(Class<T> type) {
      return Try.of(() -> objectMapper.readValue(json, type)).get();
    }

    String realmName() {
      return asRealmRepresentation().getDisplayName();
    }

    RealmRepresentation asRealmRepresentation() {
      return mapTo(RealmRepresentation.class);
    }

    @Override
    public String json() {
      return json;
    }
  }

  @Getter
  static class KeycloakHttpConfigLoaderFixtures {

    static final String CONFIG_LOCATION = "configloader/keycloak-realm-config.json";
    static final NotFoundException NOT_FOUND_EXCEPTION = new NotFoundException();
    static final ClientErrorException CLIENT_ERROR_EXCEPTION = new ClientErrorException(400);
    final KeycloakConfig config;

    KeycloakHttpConfigLoaderFixtures(ObjectMapper objectMapper) {
      config = Try.of(this::loadConfigFile)
          .map(json -> new KeycloakConfig(json, objectMapper))
          .get();
    }

    private String loadConfigFile() throws IOException {
      return IOUtils.toString(
          requireNonNull(this.getClass().getClassLoader().getResourceAsStream(CONFIG_LOCATION)),
          defaultCharset()
      );
    }
  }
}
