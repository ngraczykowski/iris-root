package com.silenteight.sens.webapp.common.support.encoding;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;

import javax.annotation.Nonnull;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
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
}
