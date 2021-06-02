package com.silenteight.serp.governance.model.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.model.common.exception.InvalidChecksumException;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChecksumCalculator {

  private static final String ALGORITHM = "MD5";

  public static String calculate(Serializable serializable) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

      objectOutputStream.writeObject(serializable);
      MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
      byte[] digest = messageDigest.digest(byteArrayOutputStream.toByteArray());
      return printHexBinary(digest);
    } catch (Exception e) {
      throw new InvalidChecksumException("Checksum cannot be calculated.", e);
    }
  }
}
