package com.silenteight.warehouse.test.generator;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

class AlertGenerator {

  private final Random random = new SecureRandom();

  Alert generate() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setPayload(convertMapToPayload(Map.of(
            "recommendation", getRandomValue("FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE"),
            "lob_country", getRandomValue("PL", "DE", "UK"),
            "risk_type", getRandomValue("SAN", "PEP"),
            "1.CustomerEntities.Address", "Fixed Value CE1",
            "2.CustomerEntities.Address", "Fixed Value CE2")))
        .addAllMatches(of(
            match(getRandomMatchName(), "solution", "NO_DECISION")))
        .build();
  }

  private static Match match(String matchName, String payloadName, String payloadSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(convertMapToPayload(Map.of(payloadName, payloadSolution)))
        .build();
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

  private String getRandomMatchName() {
    return "matches/" + randomUUID().toString();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return null;

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

}
