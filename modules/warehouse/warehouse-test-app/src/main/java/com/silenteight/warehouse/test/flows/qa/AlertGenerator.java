package com.silenteight.warehouse.test.flows.qa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaAlert;

import org.springframework.jdbc.core.JdbcTemplate;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static com.silenteight.data.api.v2.QaAlert.State.FAILED;
import static com.silenteight.data.api.v2.QaAlert.State.NEW;
import static com.silenteight.data.api.v2.QaAlert.State.PASSED;
import static com.silenteight.data.api.v2.QaAlert.State.forNumber;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class AlertGenerator {

  private static final String QA_DEFAULT_COMMENT = "Analysis made";
  private static final int[] ALERT_LEVELS = { 0, 1 };
  private static final int[] ALERT_STATES = {
      NEW.getNumber(), FAILED.getNumber(),
      PASSED.getNumber() };
  public static final String SQL_SELECT_RANDOM_ALERT_NAMES =
      "SELECT name FROM warehouse_alert order by random()";

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
        .setState(forNumber(getRandomValue(ALERT_STATES)))
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

  private int getRandomValue(int... allowedValues) {
    if (allowedValues.length < 1)
      return 0;

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
