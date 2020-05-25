package com.silenteight.serp.common.protocol;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.google.common.base.Throwables.getRootCause;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
public final class AnyUtils {

  private static final String TYPE_URL_PREFIX = "type.silenteight.com";

  @NotNull
  public static Any pack(@NonNull Message message) {
    return Any.pack(message, TYPE_URL_PREFIX);
  }

  @NotNull
  public static <T extends Message> Optional<T> maybeUnpack(
      @NonNull Any anyMessage,
      @NonNull Class<T> type) {

    try {
      return of(anyMessage.unpack(type));
    } catch (InvalidProtocolBufferException e) {
      if (log.isDebugEnabled()) {
        Throwable rootCause = getRootCause(e);
        log.debug("Unable to unpack message: type={}, exception={}, error={}, message={}",
                  type.getName(),
                  rootCause.getClass(),
                  rootCause.getMessage(),
                  TextFormat.shortDebugString(anyMessage));
      }
      return empty();
    }
  }

  @NotNull
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

  private AnyUtils() {
  }
}
