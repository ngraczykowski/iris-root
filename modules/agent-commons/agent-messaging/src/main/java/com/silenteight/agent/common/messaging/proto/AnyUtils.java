package com.silenteight.agent.common.messaging.proto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import org.jetbrains.annotations.NotNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnyUtils {

  private static final String TYPE_URL_PREFIX = "type.silenteight.com";

  @NotNull
  public static Any pack(@NonNull Message message) {
    return Any.pack(message, TYPE_URL_PREFIX);
  }

  @NotNull
  public static Any wrap(String typeName, ByteString value) {
    return Any
        .newBuilder()
        .setTypeUrl(TYPE_URL_PREFIX + "/" + typeName)
        .setValue(value)
        .build();
  }
}
