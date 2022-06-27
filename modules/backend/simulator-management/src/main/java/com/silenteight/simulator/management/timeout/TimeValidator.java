package com.silenteight.simulator.management.timeout;

import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class TimeValidator {

  public static boolean isTimeoutOnTime(OffsetDateTime timeOfEvent, OffsetDateTime timeToCompare) {
    return timeOfEvent.isBefore(timeToCompare);
  }
}
