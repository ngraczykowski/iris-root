package com.silenteight.serp.common.testing.utils;

import com.google.protobuf.ByteString;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class ByteStringTestUtils {

  private ByteStringTestUtils() {
  }

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
