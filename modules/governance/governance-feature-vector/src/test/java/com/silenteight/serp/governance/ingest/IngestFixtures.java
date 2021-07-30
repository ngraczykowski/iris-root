package com.silenteight.serp.governance.ingest;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.solving.api.v1.Feature;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;

import static java.util.List.of;

public class IngestFixtures {

  public static final String SANCTION = "SAN";
  public static final String MATCH = "MATCH";
  public static final String NO_DATA = "NO_DATA";
  public static final String DATA_SOURCE_ERROR = "DATA_SOURCE_ERROR";
  public static final Feature FEATURE_NAME_SOLUTION = Feature
      .newBuilder()
      .setName("features/name:solution")
      .build();
  public static final Feature FEATURE_GENDER_SOLUTION = Feature
      .newBuilder()
      .setName("features/gender:solution")
      .build();
  public static final Feature FEATURE_CATEGORY_AP_TYPE = Feature
      .newBuilder()
      .setName("categories/apType")
      .build();
  public static final Feature FEATURE_NAME = Feature
      .newBuilder()
      .setName("name")
      .build();
  public static final Feature FEATURE_GENDER = Feature
      .newBuilder()
      .setName("gender")
      .build();
  public static final Feature FEATURE_AP_TYPE = Feature.newBuilder()
      .setName("apType")
      .build();
  public static final String FEATURE_OR_CATEGORY_REGEX
      = "^(categories\\/(\\w)+)$|^(features\\/(\\w)+\\:solution)$";
  public static final String PREFIX_AND_SUFFIX_REGEX
      = "^(features\\/)|^(categories\\/)|(:solution)$";

  public static final Map<String, Value> FIRST_ALERT_INGEST_PAYLOAD = Map.of(
      "fvKey", getValueFromString("fv1"),
      IngestFixtures.FEATURE_NAME_SOLUTION.getName(), getValueFromString(IngestFixtures.NO_DATA),
      IngestFixtures.FEATURE_GENDER_SOLUTION.getName(), getValueFromString(IngestFixtures.MATCH),
      "features/name:config", getValueFromString("agents/name/versions/1.0.0/configs/1"),
      "features/gender:config", getValueFromString("agents/gender/versions/1.0.0/configs"),
      IngestFixtures.FEATURE_CATEGORY_AP_TYPE.getName(),
      getValueFromString(IngestFixtures.DATA_SOURCE_ERROR),
      "extendedAttribute5", getValueFromString(IngestFixtures.SANCTION));
  public static final Map<String, Value> SECOND_ALERT_INGEST_PAYLOAD = Map.of(
      "fvKey", getValueFromString("fv2"),
      IngestFixtures.FEATURE_NAME_SOLUTION.getName(), getValueFromString(IngestFixtures.MATCH),
      IngestFixtures.FEATURE_GENDER_SOLUTION.getName(), getValueFromString(IngestFixtures.MATCH),
      "features/name:config", getValueFromString("agents/name/versions/1.0.0/configs/1"),
      "features/gender:config", getValueFromString("agents/gender/versions/1.0.0/configs"),
      IngestFixtures.FEATURE_CATEGORY_AP_TYPE.getName(),
      getValueFromString(IngestFixtures.DATA_SOURCE_ERROR),
      "extendedAttribute5", getValueFromString(IngestFixtures.SANCTION));

  public static final Map<String, Value> FIRST_ALERT_LEARNING_PAYLOAD = Map.of(
      "extendedAttribute5", getValueFromString(IngestFixtures.SANCTION));

  public static final List<Alert> ALERTS = of(getAlert(FIRST_ALERT_INGEST_PAYLOAD), getAlert(
      SECOND_ALERT_INGEST_PAYLOAD));

  public static final List<Alert> LEARNING_ALERTS = of(getAlert(FIRST_ALERT_LEARNING_PAYLOAD));

  public static Value getValueFromString(String value) {
    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }

  public static Alert getAlert(Map<String, Value> alertPayload) {
    return Alert.newBuilder()
        .setPayload(getPayloadAsStruct(alertPayload))
        .build();
  }

  private static Struct getPayloadAsStruct(Map<String, Value> payload) {
    return Struct.newBuilder()
        .putAllFields(payload)
        .build();
  }
}
