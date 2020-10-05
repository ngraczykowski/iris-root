package com.silenteight.sep.base.common.support.crypto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class SecureRandomNonceGeneratorTest {

  @Test
  void given32BytesSize_generatesTwoDifferentNoncesOfGivenSize() {
    int nonceSizeInBits = 128;
    int nonceSizeInBytes = nonceSizeInBits / 4;
    var underTest = new SecureRandomNonceGenerator(nonceSizeInBits);

    byte[] first = underTest.generate();
    byte[] second = underTest.generate();

    assertThat(first).isNotEqualTo(second);
    assertThat(first).hasSize(nonceSizeInBytes);
    assertThat(second).hasSize(nonceSizeInBytes);
  }
}
