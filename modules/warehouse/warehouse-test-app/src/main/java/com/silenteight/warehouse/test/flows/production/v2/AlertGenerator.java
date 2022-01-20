package com.silenteight.warehouse.test.flows.production.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.warehouse.test.generator.DataReader;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static com.silenteight.data.api.v2.QaAlert.State.*;
import static com.silenteight.data.api.v2.QaAlert.State.forNumber;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class AlertGenerator {

  private static final String[] ALERT_NAMES = { "alerts/123", "alerts/234", "alerts/345" };
  private static final int[] ALERT_LEVELS = { 0,1 };
  private static final int[] ALERT_STATES = { NEW.getNumber(), FAILED.getNumber(),
      PASSED.getNumber() };
  private static final String SEMICOLON = ";";
  private static final String COMMA = ",";
  private static final String QA_DEFAULT_COMMENT = "Analysis made";

  private final Random random = new SecureRandom();
  private final DataReader dataReader;

  Alert generateProduction() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomAlertName())
        .setPayload(convertMapToPayload(generateRandomPayload()))
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

  public QaAlert generateQa(String alertName) {
    return QaAlert.newBuilder()
        .setName(alertName)
        .setLevel(getRandomValue(ALERT_LEVELS))
        .setState(forNumber(getRandomValue(ALERT_STATES)))
        .setComment(QA_DEFAULT_COMMENT)
        .build();
  }

  private static Value asValue(Entry<String, String> entry) {
    return Value.newBuilder().setStringValue(entry.getValue()).build();
  }

  private String getRandomDiscriminator() {
    return randomUUID().toString();
  }

  private String getRandomAlertName() {
    return "alerts/" + randomUUID();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

  private int getRandomValue(int... allowedValues) {
    if (allowedValues.length < 1)
      return 0;

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
