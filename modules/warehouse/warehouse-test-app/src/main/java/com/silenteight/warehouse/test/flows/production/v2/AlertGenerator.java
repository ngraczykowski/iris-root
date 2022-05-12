package com.silenteight.warehouse.test.flows.production.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.warehouse.test.generator.DataReader;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class AlertGenerator {

  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";
  private static final Integer MAX_MATCH_COUNT = 5;

  private final Random random = new SecureRandom();
  private final DataReader alertDataReader;
  private final DataReader matchDataReader;

  Alert generateProductionAlert() {
    String alertName = getRandomAlertName();

    List<Match> matches = IntStream.range(0, random.nextInt(MAX_MATCH_COUNT) + 1)
        .mapToObj(i -> generateProductionMatch(alertName))
        .collect(Collectors.toList());

    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(alertName)
        .setPayload(convertMapToPayload(generateAlertPayload()))
        .addAllMatches(matches)
        .build();
  }

  Match generateProductionMatch(String alertName) {
    String matchName = getRandomMatchName(alertName);

    return Match.newBuilder()
        .setName(matchName)
        .setDiscriminator(matchName)
        .setPayload(convertMapToPayload(generateMatchPayload()))
        .build();
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

  private static Builder convertMapToPayload(Map<String, String> payload) {
    Map<String, Value> convertedMap = payload.entrySet().stream()
        .collect(toMap(Entry::getKey, AlertGenerator::asValue));

    return Struct.newBuilder().putAllFields(convertedMap);
  }

  private static Value asValue(Entry<String, String> entry) {
    return Value.newBuilder().setStringValue(entry.getValue()).build();
  }

  private String getRandomDiscriminator() {
    return randomUUID().toString();
  }

  private String getRandomAlertName() {
    return String.join("/", "alerts", randomUUID().toString());
  }

  private String getRandomMatchName(String alertName) {
    return String.join("/",alertName, "matches", randomUUID().toString());
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
