package com.silenteight.serp.common.testing.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.ByteString;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProtobufTypeConversion {

  public static ByteString toByteString(String text) {
    return ByteString.copyFrom(text, StandardCharsets.UTF_8);
  }
}
