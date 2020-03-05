package com.silenteight.sens.webapp.keycloak.configloader.provider.template;

import io.vavr.control.Try;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.TemplateKeycloakConfigProviderTest.ProductionKeycloakConfigProviderFixtures.CANT_FIND_TEMPLATE_EXCEPTION;
import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.TemplateKeycloakConfigProviderTest.ProductionKeycloakConfigProviderFixtures.CANT_PARSE_TEMPLATE_EXCEPTION;
import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.TemplateKeycloakConfigProviderTest.ProductionKeycloakConfigProviderFixtures.PARSED_CONFIG;
import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.TemplateKeycloakConfigProviderTest.ProductionKeycloakConfigProviderFixtures.TEMPLATE_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TemplateKeycloakConfigProviderTest {

  @Mock
  private KeycloakConfigTemplateProvider keycloakConfigTemplateProvider;

  private TemplateKeycloakConfigProvider underTest;


  @BeforeEach
  void setUp() {
    underTest = new TemplateKeycloakConfigProvider(
        TEMPLATE_NAME,
        keycloakConfigTemplateProvider,
        ProductionKeycloakConfigProviderFixtures.CONFIG
    );
  }

  @Test
  void templateProviderReturnsTryWithException_rethrowsIt() {
    given(keycloakConfigTemplateProvider.byName(TEMPLATE_NAME))
        .willReturn(Try.failure(CANT_FIND_TEMPLATE_EXCEPTION));

    ThrowingCallable when = () -> underTest.json();

    assertThatThrownBy(when).isEqualTo(CANT_FIND_TEMPLATE_EXCEPTION);
  }

  @Test
  void keycloakConfigTemplateReturnsFailedTryOnProcess_rethrowsIt() {
    KeycloakConfigTemplate failedParsingTemplate =
        values -> Try.failure(CANT_PARSE_TEMPLATE_EXCEPTION);
    given(keycloakConfigTemplateProvider.byName(TEMPLATE_NAME))
        .willReturn(Try.success(failedParsingTemplate));

    ThrowingCallable when = () -> underTest.json();

    assertThatThrownBy(when).isEqualTo(CANT_PARSE_TEMPLATE_EXCEPTION);
  }

  @Test
  void findsTemplate_processesItCorrectlyAndReturns() {
    KeycloakConfigTemplate successfulParsingTemplate = values -> Try.success(PARSED_CONFIG);
    given(keycloakConfigTemplateProvider.byName(TEMPLATE_NAME))
        .willReturn(Try.success(successfulParsingTemplate));

    String actual = underTest.json();

    assertThat(actual).isEqualTo(PARSED_CONFIG);
  }

  static class ProductionKeycloakConfigProviderFixtures {

    static final String TEMPLATE_NAME = "configTemplate";
    static final KeycloakTemplateConfigValues CONFIG = new KeycloakTemplateConfigValues(
        Map.of(KeycloakConfigurationKey.BACKEND_BASE_URL, "backendUrl",
            KeycloakConfigurationKey.BACKEND_SECRET, "backendSecret")
    );
    static final RuntimeException CANT_FIND_TEMPLATE_EXCEPTION = new RuntimeException();
    static final RuntimeException CANT_PARSE_TEMPLATE_EXCEPTION = new RuntimeException();
    static final String PARSED_CONFIG = "someConfig";
  }
}
