package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.List.of;

public class DataIndexFixtures {

  public static final String MATCH_ID_1 = "35";
  public static final String MATCH_ID_2 = "36";
  public static final String ALERT_ID_1 = "44";
  public static final String ALERT_ID_2 = "45";
  public static final String ANALYSIS_ID = "33";
  public static final String MATCH_PAYLOAD_SOLUTION_KEY = "solution";
  public static final String MATCH_PAYLOAD_SOLUTION_EXACT_MATCH = "EXACT_MATCH";
  public static final String MATCH_PAYLOAD_SOLUTION_NO_DECISION = "NO_DECISION";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_KEY = "recommendation";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_FP = "FALSE_POSITIVE";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_MI = "MANUAL_INVESTIGATION";

  private static final String MATCH_RESOURCE_PREFIX = "matches/";
  private static final String ALERT_RESOURCE_PREFIX = "alerts/";
  private static final String ANALYSIS_RESOURCE_PREFIX = "analysis/";

  static final String REQUEST_ID = "TEST_123";
  static final String NO_DATA = "NO_DATA";
  static final String MATCH_NAME_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_1;
  static final String MATCH_NAME_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_2;
  static final String ALERT_NAME_1 = ALERT_RESOURCE_PREFIX + ALERT_ID_1;
  static final String ALERT_NAME_2 = ALERT_RESOURCE_PREFIX + ALERT_ID_2;
  static final String ANALYSIS_NAME = ANALYSIS_RESOURCE_PREFIX + ANALYSIS_ID;

  static final Alert ALERT_WITHOUT_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY,
          ALERT_PAYLOAD_RECOMMENDATION_MI))
      .addAllMatches(emptyList())
      .build();

  static final Alert ALERT_WITH_MATCHES_1 = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY,
          ALERT_PAYLOAD_RECOMMENDATION_FP))
      .addAllMatches(of(
          createMatch(MATCH_NAME_1, MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_NO_DECISION),
          createMatch(MATCH_NAME_2, MATCH_PAYLOAD_SOLUTION_KEY, NO_DATA)))
      .build();

  static final Alert ALERT_WITH_MATCHES_2 = Alert.newBuilder()
      .setName(ALERT_NAME_2)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY,
          ALERT_PAYLOAD_RECOMMENDATION_MI))
      .addAllMatches(of(
          createMatch(MATCH_NAME_1, MATCH_PAYLOAD_SOLUTION_KEY,
              MATCH_PAYLOAD_SOLUTION_EXACT_MATCH),
          createMatch(MATCH_NAME_2, MATCH_PAYLOAD_SOLUTION_KEY, NO_DATA)))
      .build();

  static final List<Alert> ALERTS_WITH_MATCHES = of(
      ALERT_WITH_MATCHES_1,
      ALERT_WITH_MATCHES_2);

  public static final DataIndexRequest DATA_INDEX_REQUEST_WITH_ALERTS =
      DataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID)
          .setAnalysisName(ANALYSIS_NAME)
          .addAllAlerts(ALERTS_WITH_MATCHES)
          .build();

  static Builder structWithValue(String key, String value) {
    return Struct.newBuilder()
        .putFields(key, Value.newBuilder()
            .setStringValue(value)
            .build());
  }

  private static Match createMatch(String matchName, String payloadName, String payloadSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(structWithValue(payloadName, payloadSolution))
        .build();
  }
}
