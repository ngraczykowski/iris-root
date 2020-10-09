package com.silenteight.sep.base.common.support.crypto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.bouncycastle.crypto.generators.SCrypt;

/**
 * Password Key Derivation Function wrapper around Bouncy Castle's Scrypt implementation.
 * http://www.bouncycastle.org/docs/docs1.5on/org/bouncycastle/crypto/generators/SCrypt.html
 */
@RequiredArgsConstructor
public class ScryptKeyDerivationFunction {

  private final ScryptParameters parameters;

  public byte[] generate(byte[] password, byte[] salt, int keySizeInBits) {

    try {
      return SCrypt.generate(
          password,
          salt,
          parameters.getPerformanceParameter(),
          parameters.getBlockSize(),
          parameters.getParallelizationParameter(),
          keySizeInBits / 8);
    } catch (Exception e) {
      throw new KeyGenerationException(e);
    }
  }


  @Value
  public static class ScryptParameters {

    int performanceParameter;
    int blockSize;
    int parallelizationParameter;

    public static ScryptParameters defaults() {
      return new ScryptParameters(16384, 8, 1);
    }
  }
}
