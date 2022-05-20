package com.silenteight.payments.bridge.common.protobuf;

import java.time.Duration;

public class DurationConverter {

  com.google.protobuf.Duration fromDuration(Duration duration) {
    return com.google.protobuf.Duration
        .newBuilder()
        .setSeconds(duration.getSeconds())
        .setNanos(duration.getNano())
        .build();
  }
}
