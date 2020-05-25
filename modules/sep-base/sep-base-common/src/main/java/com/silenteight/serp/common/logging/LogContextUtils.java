package com.silenteight.serp.common.logging;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.Printer;
import com.google.rpc.StatusOrBuilder;
import org.slf4j.MDC;

import java.util.Collection;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.stream.Collectors.joining;

public final class LogContextUtils {

  private static final Printer PRINTER = TextFormat.printer();

  private static final String KEY_ALERT_SOURCE_ID = "alert.sourceId";
  private static final String KEY_ALERT_DISCRIMINATOR = "alert.discriminator";

  public static void logAlert(@Nullable String sourceId, @Nullable String discriminator) {
    put(KEY_ALERT_SOURCE_ID, nullToEmpty(sourceId));
    put(KEY_ALERT_DISCRIMINATOR, nullToEmpty(discriminator));
  }

  public static void logAlertSourceId(@Nullable String sourceId) {
    put(KEY_ALERT_SOURCE_ID, nullToEmpty(sourceId));
  }

  public static void logCollection(String key, @Nullable Collection<?> collection) {
    if (collection == null || collection.isEmpty())
      return;

    String value = collection.stream().map(Object::toString).collect(joining(", ", "[", "]"));

    put(key, value);
  }

  public static void logMessage(String key, @Nullable MessageOrBuilder message) {
    if (message == null)
      return;

    put(key, PRINTER.shortDebugString(message));
  }

  public static void logStatus(@Nullable StatusOrBuilder status) {
    if (status == null)
      return;

    put("rpcStatus.code", Integer.toString(status.getCode()));
    put("rpcStatus.message", status.getMessage());
  }

  public static void logObject(String key, @Nullable Object value) {
    if (value == null)
      return;

    put(key, value.toString());
  }

  private static void put(String key, String value) {
    if (!value.isEmpty())
      MDC.put(key, value);
  }

  private static void put(String prefix, String key, String value) {
    if (!value.isEmpty())
      MDC.put(prefix + "." + key, value);
  }

  private LogContextUtils() {
  }
}
