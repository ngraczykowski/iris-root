package com.silenteight.sens.webapp.keycloak.freemarker;

import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigTemplate;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigurationKey;
import com.silenteight.sens.webapp.keycloak.freemarker.exception.CouldNotLoadTemplateException;
import com.silenteight.sens.webapp.keycloak.freemarker.exception.CouldNotProcessTemplateException;

import com.google.common.io.Resources;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigurationKey.*;
import static com.silenteight.sens.webapp.keycloak.freemarker.KeycloakFreemarkerTemplateProviderTest.KeycloakFreemarkerTemplateProviderFixtures.*;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

class KeycloakFreemarkerTemplateProviderTest {

  private KeycloakFreemarkerTemplateProvider underTest;

  @BeforeEach
  void setUp() throws IOException {
    var freemarkerConfig = new KeycloakFreemarkerConfiguration()
        .freemarkerConfiguration(() -> TEST_RESOURCES_DIR);

    underTest = new KeycloakFreemarkerTemplateProvider(freemarkerConfig);
  }

  @Test
  void existingTemplateWithAllKeysMatching_replacesCorrectly() {
    Try<KeycloakConfigTemplate> keycloakConfigTemplates =
        underTest.byName("keycloak-config-template.ftl.json");

    Try<String> config = processWith(keycloakConfigTemplates, ALL_TWO_VARIABLES_CONFIG);

    assertThat(config.get())
        .isEqualTo(loadExpected("keycloak-config-template-replaced-all.json"));
  }

  @Test
  void nonExistingTemplate_fails() {
    Try<KeycloakConfigTemplate> actual =
        underTest.byName("someNonExistingTemplate.ftl");

    assertThat(actual.getCause())
        .isInstanceOf(CouldNotLoadTemplateException.class);
  }

  @Test
  void oneKeyMatchingAndOneExtra_fails() {
    Try<KeycloakConfigTemplate> keycloakConfigTemplates =
        underTest.byName("keycloak-config-template.ftl.json");

    Try<String> config = processWith(keycloakConfigTemplates, EMPTY_CONFIG);

    assertThat(config.getCause())
        .isInstanceOf(CouldNotProcessTemplateException.class);
  }

  @Test
  void nestedConfiguration_replacesCorrectly() {
    Try<KeycloakConfigTemplate> keycloakConfigTemplates =
        underTest.byName("keycloak-config-template-nested.ftl.json");

    Try<String> config = processWith(keycloakConfigTemplates, NESTED_CONFIG);

    assertThat(config.get())
        .isEqualTo(loadExpected("keycloak-config-template-nested-replaced.json"));
  }

  @Test
  void existingTemplateWithEmptyConfig_fails() {
    Try<KeycloakConfigTemplate> keycloakConfigTemplates =
        underTest.byName("keycloak-config-template.ftl.json");

    Try<String> config = processWith(keycloakConfigTemplates, EMPTY_CONFIG);

    assertThat(config.getCause())
        .isInstanceOf(CouldNotProcessTemplateException.class);
  }

  @Test
  void existingTemplateWithAllKeysAndOneExtraOneExtra_replacesCorrectly() {
    Try<KeycloakConfigTemplate> keycloakConfigTemplates =
        underTest.byName("keycloak-config-template.ftl.json");

    Try<String> config = processWith(keycloakConfigTemplates, ALL_VARIABLES_WITH_EXTRA_CONFIG);

    assertThat(config.get())
        .isEqualTo(loadExpected("keycloak-config-template-replaced-all.json"));
  }


  private String loadExpected(String fileName) {
    File file = FileUtils.getFile(TEST_RESOURCES_DIR, fileName);
    return Try.of(() -> FileUtils.readFileToString(file, Charset.defaultCharset())).get();
  }

  private static Try<String> processWith(
      Try<KeycloakConfigTemplate> keycloakConfigTemplates,
      Map<KeycloakConfigurationKey, Object> variables) {
    return keycloakConfigTemplates.flatMap(
        keycloakConfigTemplate -> keycloakConfigTemplate.process(variables));
  }

  static class KeycloakFreemarkerTemplateProviderFixtures {

    static final Map<KeycloakConfigurationKey, Object> NESTED_CONFIG =
        Map.of(
            FRONTEND_BASE_URL, Map.of("first", "someUrl1",
                "second", "someUrl2"),
            FRONTEND_REDIRECT_URLS, Arrays.asList("firstRedirectUrl", "secondRedirectUrl")
        );

    static final Map<KeycloakConfigurationKey, Object> ALL_TWO_VARIABLES_CONFIG =
        Map.of(
            BACKEND_BASE_URL, "http://localhost:7070",
            BACKEND_SECRET, "theSecret"
        );
    static final Map<KeycloakConfigurationKey, Object> ALL_VARIABLES_WITH_EXTRA_CONFIG =
        Map.of(
            BACKEND_BASE_URL, "http://localhost:7070",
            BACKEND_SECRET, "theSecret",
            FRONTEND_SECRET, "frontendSecret"
        );
    static final Map<KeycloakConfigurationKey, Object> BASE_URL_AND_ONE_EXTRA_CONFIG =
        Map.of(
            BACKEND_BASE_URL, "http://localhost:7070",
            CLI_SECRET, "theCliSecret"
        );
    static final Map<KeycloakConfigurationKey, Object> EMPTY_CONFIG = emptyMap();

    static final File TEST_RESOURCES_DIR = getFile("freemarker");

    private static File getFile(String resourceName) {
      return new File(Resources.getResource(resourceName).getFile());
    }
  }
}
