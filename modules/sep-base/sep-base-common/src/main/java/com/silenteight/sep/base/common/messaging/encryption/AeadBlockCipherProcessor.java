package com.silenteight.sep.base.common.messaging.encryption;

import lombok.RequiredArgsConstructor;

import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.AEADBlockCipher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
class AeadBlockCipherProcessor {

  private final AEADBlockCipher cipher;

  byte[] process(byte[] input) {
    ByteArrayOutputStream processedStream = new ByteArrayOutputStream();

    try (CipherOutputStream cipherOutputStream = new CipherOutputStream(processedStream, cipher)) {
      cipherOutputStream.write(input);
    } catch (IOException e) {
      throw new UnableToCipherDataException(e);
    }

    return processedStream.toByteArray();
  }

  private static class UnableToCipherDataException extends RuntimeException {

    private static final long serialVersionUID = 6870681684491349701L;

    UnableToCipherDataException(Throwable cause) {
      super(cause);
    }
  }
}
