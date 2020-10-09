package com.silenteight.sep.base.common.support.crypto;

import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

@RequiredArgsConstructor
public class SecureRandomNonceGenerator implements NonceGenerator {

  private final int sizeInBits;

  @Override
  public byte[] generate() {
    SecureRandom random = new SecureRandom();
    byte[] nonce = new byte[sizeInBits / 8];
    random.nextBytes(nonce);
    return nonce;
  }
}
