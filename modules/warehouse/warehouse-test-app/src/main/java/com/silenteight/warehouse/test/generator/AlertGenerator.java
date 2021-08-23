package com.silenteight.warehouse.test.generator;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;

class AlertGenerator {

  private static final String[] ALERT_NAMES = { "alerts/123", "alerts/234", "alerts/345" };
  private static final String NO_DATA = "NO_DATA";
  private static final String INCONCLUSIVE = "INCONCLUSIVE";
  private static final String NO = "NO";

  private final Random random = new SecureRandom();

  Alert generateProduction() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomValue(ALERT_NAMES))
        .setPayload(convertMapToPayload(generateRandomPayload()))
        .build();
  }

  Alert generateSimulation() {
    return Alert.newBuilder()
        .setDiscriminator(getRandomDiscriminator())
        .setName(getRandomValue(ALERT_NAMES))
        .setPayload(convertMapToPayload(generateRandomPayload()))
        .build();
  }

  private HashMap<String, String> generateRandomPayload() {
    String date = now(UTC).format(ISO_ZONED_DATE_TIME);
    HashMap<String, String> payload = new HashMap<>();
    payload.put("status", getRandomValue("LEARNING_COMPLETED", "ERROR"));
    payload.put("index_timestamp", date);
    payload.put(
        "recommendation_recommended_action", getRandomValue(
            "ACTION_FALSE_POSITIVE",
            "ACTION_POTENTIAL_TRUE_POSITIVE",
            "ACTION_INVESTIGATE"));
    payload.put(
        "DN_CASE.currentState", getRandomValue("Level 1 Review", "Level 2 Review"));
    payload.put("recommendationMonth", getRandomValue("01", "08", "09"));
    payload.put("recommendationYear", getRandomValue("2021"));
    payload.put(
        "s8_recommendation",
        getRandomValue(
            "ACTION_FALSE_POSITIVE", "ACTION_POTENTIAL_TRUE_POSITIVE",
            "ACTION_MANUAL_INVESTIGATION"));
    payload.put("recommendationDate", date);
    payload.put(
        "fvSignature",
        getRandomValue(
            "qC4MMVPvDOpB/vA+hn8tM8mUgt4=",
            "qC4MMVPvDOpB/vA+hn8tM8mUgt4="));
    payload.put(
        "policyId", getRandomValue(
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3",
            "policies/c13b2278-f0d5-4366-a40f-576a3fb4f5a3"));
    payload.put("policy_title", getRandomValue("PROD Policy for HSBC - PoV v.16"));
    payload.put("stepId", getRandomValue(""));
    payload.put("step_title", getRandomValue(""));
    payload.put("extendedAttribute5", getRandomValue("SAN", "PEP", "SCION"));
    payload.put("features/commonAp:solution", getRandomValue(NO));
    payload.put("features/commonMp:solution", getRandomValue(NO));
    payload.put("features/commonNames:solution", getRandomValue(NO));
    payload.put("features/dateOfBirth:solution", getRandomValue(NO_DATA));
    payload.put("features/gender:solution", getRandomValue(NO_DATA));
    payload.put("features/geoPlaceOfBirth:solution", getRandomValue(NO_DATA));
    payload.put("features/geoResidencies:solution", getRandomValue(NO_DATA));
    payload.put("features/incorporationCountry:solution", getRandomValue(NO_DATA));
    payload.put("features/invalidAlert:solution", getRandomValue(NO));
    payload.put("features/isApTpMarked:solution", getRandomValue(INCONCLUSIVE));
    payload.put("features/isCaseTpMarked:solution", getRandomValue(INCONCLUSIVE));
    payload.put("features/isPep:solution", getRandomValue("DATA_SOURCE_ERROR"));
    payload.put("features/isTpMarked:solution", getRandomValue(INCONCLUSIVE));
    payload.put(
        "features/logicalDiscountingDob:solution",
        getRandomValue(INCONCLUSIVE));
    payload.put("features/name:solution", getRandomValue("DATA_SOURCE_ERROR"));
    payload.put("features/nationalIdDocument:solution", getRandomValue(NO_DATA));
    payload.put("features/otherCountry:solution", getRandomValue("MATCH", NO_DATA));
    payload.put("features/otherDocument:solution", getRandomValue(NO_DATA));
    payload.put("features/passportNumberDocument:solution", getRandomValue(NO_DATA));
    payload.put("features/registrationCountry:solution", getRandomValue(NO_DATA));
    payload.put("features/residencyCountry:solution", getRandomValue(NO_DATA));
    payload.put("categories/hitType:value", getRandomValue("SAN", "PEP", "SCION"));
    payload.put("s8_country", getRandomValue("UK", "DE"));
    payload.put(
        "qa.level-0.state",
        getRandomValue("qa_decision_PASSED", "qa_decision_FAILED"));
    payload.put(
        "analyst_decision",
        getRandomValue(
            "analyst_decision_true_positive",
            "analyst_decision_false_positive"));
    payload.put("lob_country", getRandomValue("PL", "DE", "UK"));

    return payload;
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
