package com.silenteight.serp.governance.model.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.model.common.exception.InvalidChecksumException;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChecksumCalculator {

  private static final String ALGORITHM = "MD5";

  public static String calculate(@NonNull String value) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

      objectOutputStream.writeObject(value);
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      byte[] digest = messageDigest.digest(byteArrayOutputStream.toByteArray());
      return printHexBinary(digest);
    } catch (Exception e) {
      throw new InvalidChecksumException("Checksum cannot be calculated.", e);
    }
  }
}
