package com.silenteight.warehouse.simulation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationAlertFixtures {

  public static final String COUNTRY_KEY = "lob_country";
  public static final String RECOMMENDATION_KEY = "recommendation";
  public static final String COUNTRY_VALUE = "UK";
  public static final String RECOMMENDATION_VALUE = "FALSE_POSITIVE";
  public static final String LEVEL_1_KEY = "L1";
  public static final String LEVEL_2_KEY = "L2";
  public static final String VALUE = "VALUE";
  public static final String BLANK = "";

  public static final com.silenteight.data.api.v1.Alert ALERT_EMPTY =
      Alert
          .newBuilder()
          .setDiscriminator(BLANK)
          .setName(BLANK)
          .setPayload(Struct.newBuilder().build())
          .build();


  public static final com.silenteight.data.api.v1.Alert ALERT_SIM_1 =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(DISCRIMINATOR_1)
          .setName(Values.ALERT_NAME)
          .setPayload(convertMapToPayload(Map.of(
              COUNTRY_KEY, COUNTRY_VALUE,
              RECOMMENDATION_KEY, RECOMMENDATION_VALUE
          )))
          .build();

  public static final com.silenteight.data.api.v1.Alert ALERT_NESTED_PAYLOAD =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(DISCRIMINATOR_1)
          .setName(Values.ALERT_NAME)
          .setPayload(
              singletonValue(LEVEL_1_KEY, toValue(
                  singletonValue(LEVEL_2_KEY, toValue(VALUE)))))
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

  private static Value toValue(Struct struct) {
    return Value.newBuilder().setStructValue(struct).build();
  }

  private static Struct singletonValue(String key, Value value) {
    return Struct.newBuilder()
        .putFields(key, value)
        .build();
  }
}
