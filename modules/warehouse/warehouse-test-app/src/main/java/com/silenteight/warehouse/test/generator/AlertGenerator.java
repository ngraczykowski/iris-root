package com.silenteight.warehouse.test.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class AlertGenerator {

  private static final String[] ALERT_NAMES = { "alerts/123", "alerts/234", "alerts/345" };
  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";

  private final Random random = new SecureRandom();
  private final DataReader dataReader;

  Alert generateProduction() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomAlertName())
        .setPayload(convertMapToPayload(generateRandomPayload()))
        .build();
  }

  com.silenteight.data.api.v1.Alert generateSimulation() {
    return com.silenteight.data.api.v1.Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomAlertName())
        .setPayload(convertMapToPayload(generateRandomPayload()))
        .build();
  }

  PersonalInformationExpired generatePersonalInformationExpired(List<String> alertNames) {
    return PersonalInformationExpired
        .newBuilder()
        .addAllAlerts(alertNames)
        .build();
  }

  AlertsExpired generateAlertsExpired(List<String> alertNames) {
    return AlertsExpired
        .newBuilder()
        .addAllAlerts(alertNames)
        .build();
  }

  private Map<String, String> generateRandomPayload() {
    return dataReader.getLines()
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

  private static Value asValue(Map.Entry<String, String> entry) {
    return Value.newBuilder().setStringValue(entry.getValue()).build();
  }

  private String getRandomDiscriminator() {
    return randomUUID().toString();
  }

  private String getRandomAlertName() {
    return "alerts/" + randomUUID().toString();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
