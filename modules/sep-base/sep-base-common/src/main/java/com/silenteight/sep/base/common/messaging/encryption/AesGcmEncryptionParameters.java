package com.silenteight.sep.base.common.messaging.encryption;

import lombok.Data;

import com.silenteight.sep.base.common.support.crypto.NonceGenerator;

@Data
public class AesGcmEncryptionParameters {

  private final byte[] key;
  private final NonceGenerator nonceGenerator;
  private final String nonceHeader;
  private final int macSizeInBits;


  public AesGcmEncryptionParameters(
      byte[] key, NonceGenerator nonceGenerator, String nonceHeader, int macSizeInBits) {
    boolean keyHasValidLength = key.length == 32 || key.length == 48 || key.length == 64;
    if (!keyHasValidLength)
      throw new IllegalArgumentException("AES key needs to be 128, 192 or 256 bits long");

    boolean macSizeIsValid = macSizeInBits >= 32 && macSizeInBits <= 128 && macSizeInBits % 8 == 0;
    if (!macSizeIsValid)
      throw new IllegalArgumentException("MAC size needs to be >= 32, <= 128 and dividable by 8");

    this.key = key;
    this.nonceGenerator = nonceGenerator;
    this.nonceHeader = nonceHeader;
    this.macSizeInBits = macSizeInBits;
  }
}
