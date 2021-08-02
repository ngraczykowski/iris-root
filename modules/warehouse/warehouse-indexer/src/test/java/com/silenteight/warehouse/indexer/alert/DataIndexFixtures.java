package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceMatchKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_2;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_2;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_2_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_2_2;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.COUNTRY_UK;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP_4;
import static java.util.Collections.emptyList;
import static java.util.List.of;

public class DataIndexFixtures {

  static final Map<String, String> ALERT_PAYLOAD_WITH_TWO_VALUES = Map.of(
      SourceAlertKeys.STATUS_KEY, Values.STATUS_OK,
      SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      SourceAlertKeys.COUNTRY_KEY, COUNTRY_UK
  );

  public static final Alert ALERT_1 = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR_1)
      .setName(Values.ALERT_NAME)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES))
      .addAllMatches(of(
          match(MATCH_NAME_1_1, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DECISION),
          match(MATCH_NAME_1_2, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DATA)))
      .build();

  public static final Alert ALERT_2 = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR_2)
      .setPayload(structWithValue(
          SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_MI))
      .addAllMatches(of(
          match(MATCH_NAME_2_1, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH),
          match(MATCH_NAME_2_2, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DATA)))
      .build();

  static final Alert ALERT_WITHOUT_MATCHES = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR_1)
      .setPayload(structWithValue(
          SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_MI))
      .addAllMatches(emptyList())
      .build();

  static final AlertSearchCriteria ALERT_SEARCH_CRITERIA = AlertSearchCriteria.builder()
      .timeRangeFrom(PROCESSING_TIMESTAMP)
      .timeRangeTo(PROCESSING_TIMESTAMP_4)
      .alertLimit(3)
      .filter(Map.of(MappedKeys.COUNTRY_KEY, COUNTRY_UK))
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

  public static Builder convertMapToPayload(Map<String, String> payload) {
    Builder builder = Struct.newBuilder();
    Map<String, Value> convertedMap = new HashMap<>();
    for (String key : payload.keySet()) {
      convertedMap.put(key, Value.newBuilder().setStringValue(payload.get(key)).build());
    }
    return builder.putAllFields(convertedMap);
  }
}
