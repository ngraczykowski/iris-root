package com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile;

import com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile.exception.CouldNotReadConfigFileException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.*;

class KeycloakSingleFileConfigProviderTest {

  private KeycloakSingleFileConfigProvider underTest;

  static final File TEST_RESOURCES_DIR =
      new File(getResource("configloader/provider/singlefile").getFile());

  @Nested
  class GivenExistingFile {

    private File configFile;

    @BeforeEach
    void setUp() {
      configFile = getFile(TEST_RESOURCES_DIR, "config-file.json");

      underTest = newUnderTest(configFile);
    }

    @Test
    void readsItCorrectly() throws IOException {
      String json = underTest.json();

      assertThat(json).isEqualTo(readFileToString(configFile, defaultCharset()));
    }
  }

  @Nested
  class GivenNonExistingFile {

    private File nonExistingFile = getFile(TEST_RESOURCES_DIR, "someNonExistingFile");

    @BeforeEach
    void setUp() {
      underTest = newUnderTest(nonExistingFile);
    }

    @Test
    void fails() {
      ThrowingCallable when = () -> underTest.json();

      assertThatThrownBy(when).isInstanceOf(CouldNotReadConfigFileException.class);
    }
  }

  private static KeycloakSingleFileConfigProvider newUnderTest(File file) {
    return new KeycloakSingleFileConfigProvider(file, defaultCharset());
  }
}
