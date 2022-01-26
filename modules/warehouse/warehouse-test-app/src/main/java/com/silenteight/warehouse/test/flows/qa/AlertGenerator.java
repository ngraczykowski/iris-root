package com.silenteight.warehouse.test.flows.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaAlert.State;

import com.google.protobuf.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import static com.silenteight.data.api.v2.QaAlert.State.FAILED;
import static com.silenteight.data.api.v2.QaAlert.State.NEW;
import static com.silenteight.data.api.v2.QaAlert.State.PASSED;
import static java.time.OffsetDateTime.parse;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class AlertGenerator {

  public static final String SQL_SELECT_RANDOM_ALERT_NAMES =
      "SELECT name FROM warehouse_alert order by random()";

  private static final String QA_DEFAULT_COMMENT = "Analysis made";
  private static final Integer[] ALERT_LEVELS = { 0, 0, 0, 1 };
  private static final State[] ALERT_STATES = { NEW, NEW, NEW, FAILED, PASSED };
  private static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  private final Random random = new SecureRandom();

  private final JdbcTemplate jdbcTemplate;

  List<QaAlert> generateQa(int alertCount) {
    return fetchRandomProductionAlertNames(alertCount)
        .stream()
        .map(this::generateQa)
        .collect(toList());
  }

  QaAlert generateQa(String alertName) {
    return QaAlert.newBuilder()
        .setName(alertName)
        .setLevel(getRandomValue(ALERT_LEVELS))
        .setState(getRandomValue(ALERT_STATES))
        .setTimestamp(toTimestamp(parse(PROCESSING_TIMESTAMP)))
        .setComment(QA_DEFAULT_COMMENT)
        .build();
  }

  private List<String> fetchRandomProductionAlertNames(int count) {
    List<String> alertNames = jdbcTemplate.query(
        SQL_SELECT_RANDOM_ALERT_NAMES + " limit " + count,
        (rs, rowNum) -> rs.getString("name"));

    if (alertNames.size() < count) {
      log.warn("Insufficient amount of production alerts in the database. "
          + "Generate production alerts first. "
          + "requestCount={}, actualCount={}", count, alertNames.size());
    }

    if (alertNames.isEmpty()) {
      throw new IllegalStateException("No production alerts in the database");
    }

    return alertNames;
  }

  private <T> T getRandomValue(T... allowedValues) {
    if (allowedValues.length < 1)
      throw new IllegalArgumentException("No values provided for generator");

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

  Timestamp toTimestamp(@NonNull Instant instant) {
    return Timestamp
        .newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  Timestamp toTimestamp(@NonNull OffsetDateTime offsetDateTime) {
    return toTimestamp(offsetDateTime.toInstant());
  }

}
