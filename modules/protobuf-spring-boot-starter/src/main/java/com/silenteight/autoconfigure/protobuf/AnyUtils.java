package com.silenteight.autoconfigure.protobuf;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Throwables;
import com.google.protobuf.*;

import java.util.Optional;

@Slf4j
final class AnyUtils {

  private static final String TYPE_URL_PREFIX = "type.silenteight.com";

  private AnyUtils() {
  }

  public static Any pack(@NonNull Message message) {
    return Any.pack(message, TYPE_URL_PREFIX);
  }

  public static <T extends Message> Optional<T> maybeUnpack(
      @NonNull Any anyMessage,
      @NonNull Class<T> type) {

    try {
      return Optional.of(anyMessage.unpack(type));
    } catch (InvalidProtocolBufferException e) {
      if (log.isDebugEnabled()) {
        Throwable rootCause = Throwables.getRootCause(e);
        log.debug(
            "Unable to unpack message: type={}, exception={}, error={}, message={}",
            type.getName(),
            rootCause.getClass(),
            rootCause.getMessage(),
            TextFormat.shortDebugString(anyMessage));
      }
      return Optional.empty();
    }
  }

  public static Any wrap(String typeName, ByteString value) {
    return Any
        .newBuilder()
        .setTypeUrl(TYPE_URL_PREFIX + "/" + typeName)
        .setValue(value)
        .build();
  }

  public static boolean isEmpty(Any any) {
    return any.getTypeUrl().isBlank();
  }
}
