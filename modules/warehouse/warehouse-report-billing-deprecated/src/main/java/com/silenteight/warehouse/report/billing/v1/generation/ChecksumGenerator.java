package com.silenteight.warehouse.report.billing.v1.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChecksumGenerator {

  private static final byte[] SALT = new byte[]
      { 83, 56, 95, 66, 73, 76, 76, 73, 78, 71, 95, 57, 100, 97, 51, 102, 97, 97, 101, 45, 51, 48,
        100, 98, 45, 52, 56, 56, 57, 45, 57, 53, 54, 100, 45, 102, 48, 97, 101, 57, 101, 53, 101,
        102, 56, 99, 54 };

  public static String generateChecksum(List<String> reportData) {
    String data = new String(SALT, StandardCharsets.UTF_8) + "\n" + String.join("", reportData);
    return new DigestUtils("SHA3-256").digestAsHex(data).toUpperCase();
  }
}
