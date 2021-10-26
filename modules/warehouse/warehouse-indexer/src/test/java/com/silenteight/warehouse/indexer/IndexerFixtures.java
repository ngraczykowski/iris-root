package com.silenteight.warehouse.indexer;

import lombok.NoArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceMatchKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.indexer.alert.mapping.AlertDefinition;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_2;
import static java.util.List.of;

@NoArgsConstructor
public final class IndexerFixtures {

  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";
  public static final String SIMULATION_ELASTIC_INDEX_NAME =
      "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  public static final String PRODUCTION_ELASTIC_WRITE_INDEX_NAME =
      "itest_production_alert.2021-04-15";
  public static final String PRODUCTION_ELASTIC_READ_ALIAS_NAME = "itest_production_alert";

  public static final Map<String, String> ALERT_PAYLOAD_WITH_TWO_VALUES = Map.of(
      SourceAlertKeys.STATUS_KEY, Values.STATUS_COMPLETED,
      SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      SourceAlertKeys.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, String> MATCH_PAYLOAD = Map.of(
      SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DECISION
  );

  public static final AlertDefinition ALERT_DEFINITION_1 = AlertDefinition.builder()
      .discriminator(DISCRIMINATOR_1)
      .name(Values.ALERT_NAME)
      .payload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES).build())
      .build();

  public static final Alert ALERT_1 = Alert
          .newBuilder()
          .setDiscriminator(DISCRIMINATOR_1)
          .setName(Values.ALERT_NAME)
          .setPayload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES))
          .addAllMatches(of(
              match(MATCH_NAME_1_1, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DECISION),
              match(MATCH_NAME_1_2, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DATA)))
          .build();

  public static final com.silenteight.data.api.v1.Alert ALERT_SIM_1 =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(DISCRIMINATOR_1)
          .setName(Values.ALERT_NAME)
          .setPayload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES))
          .addAllMatches(of(
              simMatch(MATCH_NAME_1_1, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DECISION),
              simMatch(MATCH_NAME_1_2, SourceMatchKeys.SOLUTION_KEY, Values.SOLUTION_NO_DATA)))
          .build();

  private static Builder structWithValue(String key, String value) {

    return Struct.newBuilder()
        .putFields(key, Value.newBuilder()
            .setStringValue(value)
            .build());
  }

  private static Match match(String matchName, String payloadName, String payloadSolution) {
    return Match
        .newBuilder()
        .setName(matchName)
        .setDiscriminator(matchName)
        .setPayload(structWithValue(payloadName, payloadSolution))
        .build();
  }

  private static com.silenteight.data.api.v1.Match simMatch(
      String matchName, String payloadName, String payloadSolution) {

    return com.silenteight.data.api.v1.Match.newBuilder()
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

  public static final Struct EMPTY_PAYLOAD = Struct.newBuilder().build();
}
