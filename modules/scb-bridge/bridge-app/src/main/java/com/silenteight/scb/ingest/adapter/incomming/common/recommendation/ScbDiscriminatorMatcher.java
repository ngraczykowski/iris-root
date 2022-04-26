package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.abs;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * This matcher has been implemented to compare discriminators from periodic and gns-rt flow. In
 * GNS-RT flow, customer sends us one common discriminator in "immediateResponseTimestamp" field.
 * Unfortunately in the GNS-RT request there might be multiple alerts, and it was a technical
 * limitations to change the structure of the JSON and pass dates for each alert separately. As it
 * was request by the client, we have to implement our time range (accuracy and learning).
 */
@Slf4j
class ScbDiscriminatorMatcher {

  private static final int TIME_RANGE_IN_SECONDS = 60;
  private static final DateTimeFormatter FORMATTER =
      ofPattern("[yyyy-MM-dd'T'HH:mm:ss'Z'][yyyy-MM-dd'T'HH:mm:ss.SSS'Z']");

  boolean match(@NonNull String value1, @NonNull String value2) {
    if (value1.equals(value2))
      return true;

    try {
      var date1 = parse(value1, FORMATTER);
      var date2 = parse(value2, FORMATTER);

      long diffInSeconds = abs(ChronoUnit.SECONDS.between(date1, date2));
      return diffInSeconds < TIME_RANGE_IN_SECONDS;
    } catch (DateTimeParseException ex) {
      log.info("Incorrect date format in discriminator", ex);
      return false;
    }
  }
}
