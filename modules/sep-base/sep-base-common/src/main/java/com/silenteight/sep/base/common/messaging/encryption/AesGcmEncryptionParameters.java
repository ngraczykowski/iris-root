package com.silenteight.sep.base.common.messaging.encryption;

import lombok.Data;

import com.silenteight.sep.base.common.support.crypto.NonceGenerator;

import java.util.Set;

@Data
class AesGcmEncryptionParameters {

  private final byte[] key;
  private final NonceGenerator nonceGenerator;
  private final String nonceHeader;
  private final int macSizeInBits;

  private static final Set<Integer> ALLOWED_MAC_VALUES = Set.of(128, 120, 112, 104, 96);

  public AesGcmEncryptionParameters(
      byte[] key, NonceGenerator nonceGenerator, String nonceHeader, int macSizeInBits) {
    boolean keyHasValidLength = key.length == 32 || key.length == 48 || key.length == 64;
    if (!keyHasValidLength)
      throw new IllegalArgumentException("AES key needs to be 128, 192 or 256 bits long");

    if (!ALLOWED_MAC_VALUES.contains(macSizeInBits))
      throw new IllegalArgumentException("MAC size needs to in {128, 120, 112, 104, 96}");

    this.key = key;
    this.nonceGenerator = nonceGenerator;
    this.nonceHeader = nonceHeader;
    this.macSizeInBits = macSizeInBits;
  }
}
