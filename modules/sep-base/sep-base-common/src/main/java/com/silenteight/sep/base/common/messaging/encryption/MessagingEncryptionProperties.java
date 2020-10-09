package com.silenteight.sep.base.common.messaging.encryption;

public interface MessagingEncryptionProperties {

  String getKeySeed();

  String getSalt();

  String getNonceHeader();

  int getKeySizeInBits();

  int getNonceSizeInBits();

  int getMacSizeInBits();
}
