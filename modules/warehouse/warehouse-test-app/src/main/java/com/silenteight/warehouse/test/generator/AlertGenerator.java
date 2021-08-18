package com.silenteight.warehouse.test.generator;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

class AlertGenerator {

  private static final String[] ALERT_NAMES = { "alerts/123", "alerts/234", "alerts/345" };

  private final Random random = new SecureRandom();

  Alert generateProduction() {
    HashMap<String, String> payload = new HashMap<>();
    payload.put("recommendation", getRandomValue("Level 2 Review", "AAA False Positive"));
    payload.put("comment", getRandomValue("S8 recommended action - comment"));
    payload.put("recommendationMonth", getRandomValue("01", "08", "09"));
    payload.put("recommendationYear", getRandomValue("2021"));
    payload.put(
        "s8_recommendation",
        getRandomValue(
            "ACTION_FALSE_POSITIVE", "ACTION_POTENTIAL_TRUE_POSITIVE",
            "ACTION_INVESTIGATE"));
    payload.put(
        "recommendationDate", now(UTC).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    payload.put("lob_country", getRandomValue("PL", "DE", "UK"));
    payload.put("risk_type", getRandomValue("SAN", "PEP"));
    payload.put(
        "fvSignature",
        getRandomValue("qC4MMVPvDOpB/vA+hn8tM8mUgt4=", "qC4MMVPvDOpB/vA+hn8tM8mUgt4="));
    payload.put(
        "policyId", getRandomValue(
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3",
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3"));
    payload.put(
        "policy", getRandomValue(
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3",
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3"));
    payload.put(
        "stepId", getRandomValue(
            "steps/b583e1cf-7f7c-4689-8df5-c7996763bd93",
            "steps/4461c9c8-4980-4d63-9fc8-afe3e677eacf"));
    payload.put(
        "step", getRandomValue(
            "steps/b583e1cf-7f7c-4689-8df5-c7996763bd93",
            "steps/4461c9c8-4980-4d63-9fc8-afe3e677eacf"));
    payload.put("1.CustomerEntities.LoB Region", getRandomValue("EGY", "ASP"));
    payload.put("1.CustomerEntities.Address", "Fixed Value CE1");
    payload.put("2.CustomerEntities.Address", "Fixed Value CE2");
    payload.put("DN_CASE.ExtendedAttribute5", getRandomValue("SAN", "PEP"));
    payload.put("status", getRandomValue("COMPLETED", "ERROR"));

    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomValue(ALERT_NAMES))
        .setPayload(convertMapToPayload(payload))
        .build();
  }

  Alert generateSimulation() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomValue(ALERT_NAMES))
        .setPayload(convertMapToPayload(Map.of(
            "recommendation", getRandomValue("FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE"))))
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

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return null;

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

}
