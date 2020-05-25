package com.silenteight.serp.common.protocol;

import lombok.NonNull;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;

import javax.annotation.Nonnull;

public final class ByteStringUtils {

  @Nonnull
  public static String toBase64String(@NonNull ByteString byteString) {
    return BaseEncoding.base64().encode(byteString.toByteArray());
  }

  @Nonnull
  public static ByteString fromBase64String(@NonNull String string) {
    byte[] decodedBytes = BaseEncoding.base64().decode(string);
    return ByteString.copyFrom(decodedBytes);
  }

  private ByteStringUtils() {
  }
}
