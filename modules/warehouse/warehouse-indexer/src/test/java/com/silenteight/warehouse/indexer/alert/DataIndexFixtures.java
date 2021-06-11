package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static java.util.Collections.emptyList;
import static java.util.List.of;

public class DataIndexFixtures {

  static final String INDEX_NAME = "test-index-name";

  static final Map<String, String> ALERT_PAYLOAD_WITH_TWO_VALUES =
      Map.of(ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
          ALERT_COUNTRY_KEY, ALERT_COUNTRY
      );

  static final Alert ALERT_WITH_MATCHES_1 = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES))
      .addAllMatches(of(
          match(MATCH_NAME_1_1, MATCH_SOLUTION_KEY, MATCH_SOLUTION_NO_DECISION),
          match(MATCH_NAME_1_2, MATCH_SOLUTION_KEY, NO_DATA)))
      .build();

  static final Alert ALERT_WITH_MATCHES_2 = Alert.newBuilder()
      .setName(ALERT_NAME_2)
      .setPayload(structWithValue(
          ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_MI))
      .addAllMatches(of(
          match(MATCH_NAME_2_1, MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH),
          match(MATCH_NAME_2_2, MATCH_SOLUTION_KEY, NO_DATA)))
      .build();

  public static final List<Alert> ALERTS_WITH_MATCHES = of(
      ALERT_WITH_MATCHES_1,
      ALERT_WITH_MATCHES_2);

  static final Alert ALERT_WITHOUT_MATCHES = Alert.newBuilder()
      .setName(ALERT_NAME_1)
      .setPayload(structWithValue(
          ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_MI))
      .addAllMatches(emptyList())
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

  private static Builder convertMapToPayload(Map<String, String> payload) {
    Builder builder = Struct.newBuilder();
    Map<String, Value> convertedMap = new HashMap<>();
    for (String key : payload.keySet()) {
      convertedMap.put(key, Value.newBuilder().setStringValue(payload.get(key)).build());
    }
    return builder.putAllFields(convertedMap);
  }
}
