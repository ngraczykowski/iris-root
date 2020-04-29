package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrations;
import com.silenteight.sens.webapp.keycloak.configmigration.Migration;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakConfigTemplateParser;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakTemplateProperties;

import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Optional;

import static com.google.common.io.Resources.getResource;
import static com.silenteight.sens.webapp.keycloak.configmigration.MigrationAssertion.assertThatMigration;
import static com.silenteight.sens.webapp.keycloak.configmigration.MigrationPolicy.ON_EVERY_RUN;
import static com.silenteight.sens.webapp.keycloak.configmigration.MigrationPolicy.ON_FIRST_RUN;
import static com.silenteight.sens.webapp.keycloak.configmigration.PartialMigrationAssertion.assertThatPartialMigration;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ResourcesKeycloakMigrationsLoaderTest {

  protected ResourcesKeycloakMigrationsLoader underTest;
  protected TestMigrationsLoaderProperties.Builder configBuilder;

  @Spy
  protected KeycloakConfigTemplateParser simpleTemplateParser = new NoopTemplateParser();

  static final File TEST_RESOURCES_DIR =
      new File(getResource("migrations").getFile());
  private KeycloakTemplateProperties emptyTemplateProperties = Optional::empty;

  @BeforeEach
  void setUp() {
    configBuilder = TestMigrationsLoaderProperties.builder()
        .charset(defaultCharset())
        .migrationsNameSeparator("-")
        .templateFilenameChecker(templateFilename -> templateFilename.endsWith(".ftl"))
        .configTemplateProperties(emptyTemplateProperties);
  }

  static String readTestFile(String... path) {
    return Try
        .of(() -> readFileToString(FileUtils.getFile(TEST_RESOURCES_DIR, path), defaultCharset()))
        .get();
  }

  void createUnderTest(String migrationsDir) {
    var config = new ResourcesKeycloakMigrationLoaderConfiguration();
    underTest = config.resourcesKeycloakMigrationsLoader(
        configBuilder.migrationsPath("classpath:migrations/" + migrationsDir + "/*").build(),
        simpleTemplateParser);
  }

  @Test
  void oneNonTemplateMigration_loadsCorrectly() {
    createUnderTest("just-base-migration");

    KeycloakMigrations actual = underTest.load();

    assertThat(actual.partialMigrations()).isEmpty();
    Optional<Migration> actualBaseMigration = actual.baseMigration();
    assertThat(actualBaseMigration).isNotEmpty();
    assertThatMigration(actualBaseMigration.get())
        .hasJson(readTestFile("just-base-migration", "0-migration.json"))
        .hasName("0-migration.json");
  }

  @Test
  void oneTemplateMigration_loadsCorrectly() {
    createUnderTest("just-base-template-migration");
    String expectedJson =
        readTestFile("just-base-template-migration", "0-migration.json.ftl");

    KeycloakMigrations actual = underTest.load();

    assertThat(actual.partialMigrations()).isEmpty();
    Optional<Migration> actualBaseMigration = actual.baseMigration();
    assertThat(actualBaseMigration).isNotEmpty();
    assertThatMigration(actualBaseMigration.get())
        .hasJson(expectedJson)
        .hasName("0-migration.json.ftl");
    then(simpleTemplateParser).should().parse(expectedJson, emptyTemplateProperties);
  }

  @Test
  void noTemplates_returnsEmpty() {
    createUnderTest("*.nonExisting");

    KeycloakMigrations actual = underTest.load();

    assertThat(actual.baseMigration()).isEmpty();
    assertThat(actual.partialMigrations()).isEmpty();
  }

  @Test
  void threeCorrectMigrations_loadsCorrectly() {
    createUnderTest("three-migrations");

    KeycloakMigrations actual = underTest.load();

    String baseJson = readTestFile("three-migrations", "0-base.json.ftl");
    String firstPartialJson = readTestFile("three-migrations", "1-onEveryRun-firstPartial.json");
    String secondPartialJson =
        readTestFile("three-migrations", "2-onFirstRun-secondPartial.json.ftl");
    assertThat(actual.partialMigrations()).hasSize(2)
        .anySatisfy(partialMigration -> assertThatPartialMigration(partialMigration)
            .hasJson(firstPartialJson)
            .hasPolicy(ON_EVERY_RUN)
            .hasOrdinal(1)
            .hasName("1-onEveryRun-firstPartial.json"))
        .anySatisfy(partialMigration -> assertThatPartialMigration(partialMigration)
            .hasJson(secondPartialJson)
            .hasPolicy(ON_FIRST_RUN)
            .hasOrdinal(2)
            .hasName("2-onFirstRun-secondPartial.json.ftl"));
    Optional<Migration> actualBaseMigration = actual.baseMigration();
    assertThat(actualBaseMigration).isNotEmpty();
    assertThatMigration(actual.baseMigration().get())
        .hasJson(baseJson)
        .hasName("0-base.json.ftl");
    then(simpleTemplateParser).should().parse(baseJson, emptyTemplateProperties);
    then(simpleTemplateParser).should().parse(secondPartialJson, emptyTemplateProperties);
  }
}
