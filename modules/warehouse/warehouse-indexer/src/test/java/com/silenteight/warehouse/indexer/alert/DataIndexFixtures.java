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

  static final String REQUEST_ID = "TEST_123";
  static final String NO_DATA = "NO_DATA";
  static final String MANUAL_INVESTIGATION = "MANUAL_INVESTIGATION";
  static final String FALSE_POSITIVE = "FALSE_POSITIVE";
  static final String EXACT_MATCH = "EXACT_MATCH";
  static final String WEAK_MATCH = "WEAK_MATCH";
  static final String NO_DECISION = "NO_DECISION";
  static final String SOLUTION = "solution";
  static final String RECOMMENDATION = "recommendation";
  static final String MATCH_NAME_35 = "matches/35";
  static final String MATCH_NAME_36 = "matches/36";
  static final String ALERT_NAME_44 = "alerts/44";
  static final String ALERT_NAME_45 = "alerts/45";
  static final String ANALYSIS_NAME_35 = "analysis/35";

  static final Alert ALERT_WITH_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME_44)
      .setPayload(structWithValue(RECOMMENDATION, FALSE_POSITIVE))
      .addAllMatches(of(
          createMatch(MATCH_NAME_35, SOLUTION, NO_DECISION),
          createMatch(MATCH_NAME_36, SOLUTION, NO_DATA)))
      .build();

  static final List<Alert> ALERTS_WITH_MATCHES = of(
      Alert.newBuilder()
          .setName(ALERT_NAME_44)
          .setPayload(structWithValue(RECOMMENDATION, FALSE_POSITIVE))
          .addAllMatches(of(
              createMatch(MATCH_NAME_35, SOLUTION, WEAK_MATCH),
              createMatch(MATCH_NAME_36, SOLUTION, NO_DATA)))
          .build(),
      Alert.newBuilder()
          .setName(ALERT_NAME_45)
          .setPayload(structWithValue(RECOMMENDATION, MANUAL_INVESTIGATION))
          .addAllMatches(of(
              createMatch(MATCH_NAME_35, SOLUTION, EXACT_MATCH),
              createMatch(MATCH_NAME_36, SOLUTION, NO_DATA)))
          .build());

  public static final DataIndexRequest DATA_INDEX_REQUEST_WITH_ALERTS =
      DataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID)
          .setAnalysisName(ANALYSIS_NAME_35)
          .addAllAlerts(ALERTS_WITH_MATCHES)
          .build();

  static final Alert ALERT_WITHOUT_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME_44)
      .setPayload(structWithValue(RECOMMENDATION, MANUAL_INVESTIGATION))
      .addAllMatches(emptyList())
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
