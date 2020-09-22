package com.silenteight.sens.webapp.user.password;

import com.silenteight.sens.webapp.user.password.SensCompatiblePasswordGenerator.SensPasswordGeneratorConfig;
import com.silenteight.sens.webapp.user.password.SensCompatiblePasswordGenerator.StringGenerator;
import com.silenteight.sep.usermanagement.api.TemporaryPassword;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.password.PasswordAssert.assertThatPassword;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SensCompatiblePasswordGeneratorTest {

  @Mock
  private StringGenerator alphabetic;
  @Mock
  private StringGenerator alphanumeric;
  @Mock
  private StringGenerator numeric;

  @Test
  void generatesCorrectly() {
    var config = new SensPasswordGeneratorConfig(8, 1, 1);
    var randomAlphabetic = "a";
    var randomAlphanumeric = "a6G2B6";
    var randomNumber = "5";

    given(alphabetic.generate(1)).willReturn(randomAlphabetic);
    given(alphanumeric.generate(6)).willReturn(randomAlphanumeric);
    given(numeric.generate(1)).willReturn(randomNumber);

    var underTest = createUnderTest(config);

    TemporaryPassword actual = underTest.generate();

    String password = actual.getPassword();

    assertThatPassword(password)
        .hasAtLeastOneLowercaseLetter()
        .hasAtLeastOneNumber()
        .containsAllCharactersInAnyOrder(randomAlphabetic + randomAlphabetic + randomNumber);
  }

  private SensCompatiblePasswordGenerator createUnderTest(SensPasswordGeneratorConfig config) {
    return new SensCompatiblePasswordGenerator(config, alphabetic, alphanumeric, numeric);
  }
}
