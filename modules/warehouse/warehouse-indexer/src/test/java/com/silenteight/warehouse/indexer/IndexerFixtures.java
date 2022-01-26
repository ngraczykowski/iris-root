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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_NAME_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.MATCH_NAME_1_2;
import static java.util.Collections.emptyMap;
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

  public static final AlertDefinition ALERT_DEFINITION_1 = AlertDefinition
      .builder()
      .discriminator(DISCRIMINATOR_1)
      .name(ALERT_NAME_1)
      .payload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES).build())
      .build();

  private static final Map<String, Value> FEATURES_PAYLOAD = Map
      .of("features/name", toValue("EXACT"));
  public static final Alert ALERT_1 = Alert
      .newBuilder()
      .setDiscriminator(DISCRIMINATOR_1)
      .setName(ALERT_NAME_1)
      .setPayload(convertMapToPayload(ALERT_PAYLOAD_WITH_TWO_VALUES))
      .addAllMatches(of(
          match(MATCH_NAME_1_1,
              SourceMatchKeys.SOLUTION_KEY,
              Values.SOLUTION_NO_DECISION,
              FEATURES_PAYLOAD),
          match(MATCH_NAME_1_2,
              SourceMatchKeys.SOLUTION_KEY,
              Values.SOLUTION_NO_DATA,
              FEATURES_PAYLOAD)))
      .build();

  private static Match match(
      String matchName, String payloadName, String payloadSolution, Map<String, Value> features) {
    return Match
        .newBuilder()
        .setName(matchName)
        .setDiscriminator(matchName)
        .setPayload(structWithValues(payloadName, payloadSolution, features))
        .build();
  }

  private static com.silenteight.data.api.v1.Match simMatch(
      String matchName, String payloadName, String payloadSolution) {

    return com.silenteight.data.api.v1.Match
        .newBuilder()
        .setName(matchName)
        .setPayload(structWithValues(payloadName, payloadSolution, emptyMap()))
        .build();
  }

  private static Struct structWithValues(
      String key, String value, Map<String, Value> features) {
    Struct featuresStruct = Struct.newBuilder().putAllFields(features).build();
    Value featuresValue = Value.newBuilder().setStructValue(featuresStruct).build();
    return Struct
        .newBuilder()
        .putFields(key, toValue(value))
        .putFields("features", featuresValue)
        .build();
  }

  public static Builder convertMapToPayload(Map<String, String> payload) {
    Builder builder = Struct.newBuilder();
    Map<String, Value> convertedMap = new HashMap<>();
    for (String key : payload.keySet()) {
      convertedMap.put(key, toValue(payload.get(key)));
    }
    return builder.putAllFields(convertedMap);
  }

  @NotNull
  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }

  public static final Struct EMPTY_PAYLOAD = Struct.newBuilder().build();
}
