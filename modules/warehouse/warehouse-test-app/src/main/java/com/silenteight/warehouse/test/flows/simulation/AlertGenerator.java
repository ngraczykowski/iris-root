package com.silenteight.warehouse.test.flows.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.test.generator.DataReader;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
class AlertGenerator {

  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";
  public static final String SQL_SELECT_RANDOM_ALERT_NAMES =
      "SELECT name FROM warehouse_alert order by random()";

  private final Random random = new SecureRandom();
  private final DataReader dataReader;
  private final JdbcTemplate jdbcTemplate;

  List<Alert> generateSimulationAlerts(int alertCount) {
    return fetchRandomProductionAlertNames(alertCount)
        .stream()
        .map(this::generateSimulationAlert)
        .collect(toList());
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

  private Alert generateSimulationAlert(String alertName) {
    return Alert.newBuilder()
        .setDiscriminator(alertName)
        .setName(alertName)
        .setPayload(convertMapToPayload(generateRandomPayload(alertName)))
        .build();
  }

  private static Builder convertMapToPayload(Map<String, String> payload) {
    Map<String, Value> convertedMap = payload.entrySet().stream()
        .collect(toMap(Entry::getKey, AlertGenerator::asValue));

    return Struct.newBuilder().putAllFields(convertedMap);
  }

  private Map<String, String> generateRandomPayload(String alertName) {
    Map<String, String> payload = dataReader.getLines()
        .stream()
        .map(line -> line.split(SEMICOLON))
        .collect(toMap(fieldName -> fieldName[0], values -> getValue(values[1])));
    payload.put("recommendation_alert", alertName);

    return payload;
  }

  private String getValue(String values) {
    String[] splitValues = values.split(COMMA);
    return getRandomValue(splitValues);
  }

  private static Value asValue(Entry<String, String> entry) {
    return Value.newBuilder().setStringValue(entry.getValue()).build();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
