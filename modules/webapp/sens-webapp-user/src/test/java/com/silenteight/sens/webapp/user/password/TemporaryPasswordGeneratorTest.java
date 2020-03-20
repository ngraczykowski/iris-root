package com.silenteight.sens.webapp.user.password;

import com.silenteight.sens.webapp.user.password.TemporaryPasswordGenerator.AlphanumericStringGenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TemporaryPasswordGeneratorTest {

  private int requestedPasswordSize = 3;

  private String generatedPassword = "password";

  private AlphanumericStringGenerator stringGenerator =
      size -> size == requestedPasswordSize ? generatedPassword : "";

  private TemporaryPasswordGenerator underTest =
      new TemporaryPasswordGenerator(requestedPasswordSize, stringGenerator);

  @Test
  void generatesProperly() {
    TemporaryPassword actual = underTest.generate();

    assertThat(actual)
        .extracting(TemporaryPassword::getPassword)
        .isEqualTo(generatedPassword);
  }
}
