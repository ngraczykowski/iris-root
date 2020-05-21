package com.silenteight.sens.webapp.grpc.util;

import lombok.NoArgsConstructor;

import com.google.protobuf.ByteString;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = PRIVATE)
public final class ByteStringTestUtils {

  public static ByteString randomSignature() {
    return createSignature(randomString());
  }

  public static ByteString createSignature(String text) {
    return ByteString.copyFrom(text.getBytes());
  }

  private static String randomString() {
    return randomAlphabetic(5);
  }
}
