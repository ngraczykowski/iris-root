package com.silenteight.warehouse.indexer.simulation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationAlertFixtures {

  public static final String MATCH_NAME_1 = "alerts/12/matches/24";
  public static final String MATCH_NAME_2 = "alerts/12/matches/36";
  public static final String ANALYST_DECISION_KEY = "analyst_decision";
  public static final String ANALYST_DECISION_1_VALUE = "analyst_decision_false_positive";
  public static final String ANALYST_DECISION_2_VALUE = "analyst_decision_potential_true_positive";
  public static final Map<String, String> MATCH_1_PAYLOAD = Map.of(
      ANALYST_DECISION_KEY, ANALYST_DECISION_1_VALUE);
  public static final Map<String, String> MATCH_2_PAYLOAD = Map.of(
      ANALYST_DECISION_KEY, ANALYST_DECISION_2_VALUE);
  public static final String INDEX_NAME = "simulation_index";
  public static final String ALERT_NAME = "alerts/12";
  public static final String ALERT_DISCRIMINATOR = "2Fc01e5d49-a72d-4e6a-b634-265165916c9a";
  public static final String COUNTRY_KEY = "lob_country";
  public static final String RECOMMENDATION_KEY = "recommendation";
  public static final String STATUS_KEY = "status";
  public static final String COUNTRY_VALUE = "UK";
  public static final String RECOMMENDATION_VALUE = "FALSE_POSITIVE";
  public static final String STATUS_VALUE = "COMPLETED";
  public static final Map<String, String> ALERT_PAYLOAD = Map.of(
      COUNTRY_KEY, COUNTRY_VALUE,
      RECOMMENDATION_KEY, RECOMMENDATION_VALUE);

  public static final Match MATCH_1 = Match.newBuilder()
      .setName(MATCH_NAME_1)
      .setPayload(convertMapToPayload(MATCH_1_PAYLOAD))
      .build();

  public static final Match MATCH_2 = Match.newBuilder()
      .setName(MATCH_NAME_2)
      .setPayload(convertMapToPayload(MATCH_2_PAYLOAD))
      .build();

  public static final Alert ALERT_WITH_NO_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME)
      .setDiscriminator(ALERT_DISCRIMINATOR)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD))
      .build();

  public static final Alert ALERT_WITH_ONE_MATCH = Alert.newBuilder()
      .setName(ALERT_NAME)
      .setDiscriminator(ALERT_DISCRIMINATOR)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD))
      .addAllMatches(List.of(MATCH_1))
      .build();

  public static final Alert ALERT_WITH_TWO_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME)
      .setDiscriminator(ALERT_DISCRIMINATOR)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD))
      .addAllMatches(List.of(MATCH_1, MATCH_2))
      .build();

  private static Struct convertMapToPayload(Map<String, String> payload) {
    return Struct.newBuilder()
        .putAllFields(toValues(payload))
        .build();
  }

  private static Map<String, Value> toValues(Map<String, String> payload) {
    return payload
        .keySet()
        .stream()
        .collect(Collectors.toMap(key -> key, key -> toValue(payload.get(key))));
  }

  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }

  public static final Map<String, Object> PRODUCTION_DATA = Map.of(STATUS_KEY, STATUS_VALUE);
  public static final Map<String, Object> ALERT_DATA = Map.of(
      COUNTRY_KEY, COUNTRY_VALUE,
      RECOMMENDATION_KEY, RECOMMENDATION_VALUE);
  public static final Map<String, Object> MATCH_DATA = Map.of(
      COUNTRY_KEY, COUNTRY_VALUE,
      RECOMMENDATION_KEY, RECOMMENDATION_VALUE,
      ANALYST_DECISION_KEY, ANALYST_DECISION_1_VALUE);
}
