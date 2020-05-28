package com.silenteight.sep.base.testing.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.ByteString;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
