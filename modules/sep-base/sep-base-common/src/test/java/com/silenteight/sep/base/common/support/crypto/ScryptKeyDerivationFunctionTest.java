package com.silenteight.sep.base.common.support.crypto;

import com.silenteight.sep.base.common.support.crypto.ScryptKeyDerivationFunction.ScryptParameters;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class ScryptKeyDerivationFunctionTest {

  @Test
  void givenSamePasswordAndSalt_twiceGeneratesEqualBytes() {
    byte[] password = "password".getBytes(UTF_8);
    byte[] salt = "salt".getBytes(UTF_8);
    var params = ScryptParameters.defaults();
    var underTest = new ScryptKeyDerivationFunction(params);

    byte[] firstGenerated = underTest.generate(password, salt, 256);
    byte[] secondGenerated = underTest.generate(password, salt, 256);

    assertThat(firstGenerated).isEqualTo(secondGenerated);
  }

  @Test
  void generatesBytesOfGivenKeySize() {
    int keySizeInBits = 128;
    var params = ScryptParameters.defaults();
    var underTest = new ScryptKeyDerivationFunction(params);

    byte[] firstGenerated = underTest.generate(
        "password".getBytes(UTF_8), "salt".getBytes(UTF_8), keySizeInBits);

    assertThat(firstGenerated).hasSize(keySizeInBits / 8);
  }

  @Test
  void givenInvalidParameters_throwsKeyGenerationException() {
    var params = new ScryptParameters(-22222, -22222, -22222);
    var underTest = new ScryptKeyDerivationFunction(params);

    ThrowingCallable when =
        () -> underTest.generate("password".getBytes(UTF_8), "salt".getBytes(UTF_8), 128);

    assertThatThrownBy(when).isInstanceOf(KeyGenerationException.class);
  }
}
