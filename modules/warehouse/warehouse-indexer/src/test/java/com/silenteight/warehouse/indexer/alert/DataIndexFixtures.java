package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.List;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static java.util.Collections.emptyList;
import static java.util.List.of;

public class DataIndexFixtures {

  static final String INDEX_NAME = "test-index-name";

  static final Alert ALERT_WITH_MATCHES_1 = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_FP))
      .addAllMatches(of(
          match(MATCH_NAME_1, MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_NO_DECISION),
          match(MATCH_NAME_2, MATCH_PAYLOAD_SOLUTION_KEY, NO_DATA)))
      .build();

  static final Alert ALERT_WITH_MATCHES_2 = Alert.newBuilder()
      .setName(ALERT_NAME_2)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_MI))
      .addAllMatches(of(
          match(MATCH_NAME_1, MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_EXACT_MATCH),
          match(MATCH_NAME_2, MATCH_PAYLOAD_SOLUTION_KEY, NO_DATA)))
      .build();

  public static final List<Alert> ALERTS_WITH_MATCHES = of(
      ALERT_WITH_MATCHES_1,
      ALERT_WITH_MATCHES_2);

  static final Alert ALERT_WITHOUT_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(structWithValue(
          ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_MI))
      .addAllMatches(emptyList())
      .build();

  public static final DataIndexRequest DATA_INDEX_REQUEST_WITH_ALERTS =
      DataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID)
          .setAnalysisName(ANALYSIS)
          .addAllAlerts(ALERTS_WITH_MATCHES)
          .build();

  public static final DataIndexRequest DATA_INDEX_REQUEST_WITHOUT_MATCHES =
      DataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID)
          .setAnalysisName(ANALYSIS)
          .addAlerts(0, ALERT_WITHOUT_MATCHES)
          .build();

  static Builder structWithValue(String key, String value) {
    return Struct.newBuilder()
        .putFields(key, Value.newBuilder()
            .setStringValue(value)
            .build());
  }

  private static Match match(String matchName, String payloadName, String payloadSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(structWithValue(payloadName, payloadSolution))
        .build();
  }
}
