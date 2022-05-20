package com.silenteight.warehouse.test.flows.simulation.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.SimulationAlert;
import com.silenteight.data.api.v2.SimulationMatch;
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
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
class AlertGenerator {

  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";
  public static final Function<Integer, String> SQL_SELECT_RANDOM_ALERT_NAMES = limit ->
      "WITH selected_alerts AS ("
          + " SELECT "
          + "  wa.name as alert_name,"
          + "  wa.id as alert_id "
          + " FROM warehouse_alert wa "
          + " ORDER BY random() "
          + " LIMIT " + limit + ")"
          + "SELECT "
          + " sa.alert_name as alert_name,"
          + " wm.name as match_name "
          + "FROM selected_alerts sa "
          + "JOIN warehouse_match wm ON wm.alert_id=sa.alert_id ";

  private final Random random = new SecureRandom();
  private final DataReader alertDataReader;
  private final DataReader matchDataReader;
  private final JdbcTemplate jdbcTemplate;

  List<SimulationAlert> generateSimulationAlerts(int alertCount) {
    return fetchRandomProductionAlertNames(alertCount)
        .entrySet()
        .stream()
        .map(this::generateSimulationAlert)
        .collect(toList());
  }

  private Map<String, List<String>> fetchRandomProductionAlertNames(int count) {
    List<AlertMatchPair> alertMatchPairs = jdbcTemplate.query(
        SQL_SELECT_RANDOM_ALERT_NAMES.apply(count),
        (rs, rowNum) -> AlertMatchPair.builder()
            .alertName(rs.getString("alert_name"))
            .matchName(rs.getString("match_name"))
            .build());

    Map<String, List<String>> matchesByAlertName = alertMatchPairs.stream()
        .collect(groupingBy(
            AlertMatchPair::getAlertName,
            mapping(AlertMatchPair::getMatchName, toList())));

    if (matchesByAlertName.size() < count) {
      log.warn("Insufficient amount of production alerts in the database. "
          + "Generate production alerts first. "
          + "requestCount={}, actualCount={}", count, alertMatchPairs.size());
    }

    if (matchesByAlertName.keySet().isEmpty()) {
      throw new IllegalStateException("No production alerts in the database");
    }

    return matchesByAlertName;
  }

  private SimulationAlert generateSimulationAlert(Entry<String, List<String>> alertsWithMatches) {
    String alertName = alertsWithMatches.getKey();
    List<String> matchNames = alertsWithMatches.getValue();

    return SimulationAlert.newBuilder()
        .setName(alertName)
        .setPayload(convertMapToPayload(generateAlertPayload()))
        .addAllMatches(matchNames.stream()
            .map(this::generateSimulationMatch)
            .collect(toList()))
        .build();
  }

  private SimulationMatch generateSimulationMatch(String matchName) {
    return SimulationMatch.newBuilder()
        .setName(matchName)
        .setPayload(convertMapToPayload(generateMatchPayload()))
        .build();
  }

  private static Builder convertMapToPayload(Map<String, String> payload) {
    Map<String, Value> convertedMap = payload.entrySet().stream()
        .collect(toMap(Entry::getKey, AlertGenerator::asValue));

    return Struct.newBuilder().putAllFields(convertedMap);
  }

  private Map<String, String> generateAlertPayload() {
    return alertDataReader.getLines()
        .stream()
        .map(line -> line.split(SEMICOLON))
        .collect(toMap(fieldName -> fieldName[0], values -> getValue(values[1])));
  }

  private Map<String, String> generateMatchPayload() {
    return matchDataReader.getLines()
        .stream()
        .map(line -> line.split(SEMICOLON))
        .collect(toMap(fieldName -> fieldName[0], values -> getValue(values[1])));
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

  @lombok.Builder
  @lombok.Value
  @RequiredArgsConstructor
  static class AlertMatchPair {

    String alertName;
    String matchName;
  }
}
