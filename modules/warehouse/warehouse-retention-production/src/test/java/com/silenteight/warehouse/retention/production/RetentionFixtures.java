package com.silenteight.warehouse.retention.production;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.dataretention.api.v1.AlertsExpired;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.Map;

import static java.util.List.of;
import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetentionFixtures {

  public static final String ANALYST_COMMENT_FIELD = "alertAnalystDecision";
  public static final String COMMENT_CORRECT = "correct resolved";
  public static final String ALERT_NAME_FIELD = "s8_alert_name";
  public static final String ALERT_NAME_1 = "alerts/457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_NAME_2 = "alerts/168c0428-5b9d-4db7-8e11-86264356433f";
  public static final String MATCH_NAME_1 = "matches/d25641e9-71d6-4705-a90b-5bc3d5303425";
  public static final String DISCRIMINATOR_FIELD = "s8_discriminator";
  public static final String DISCRIMINATOR_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String DISCRIMINATOR_2 = "df3dbd00-4800-4d59-b53e-5bb6c7eb4395";
  public static final String MATCH_DISCRIMINATOR_1 = "de26c0cc-a6cc-49c0-a0fb-3f1999ca1e31";
  public static final String RECOMMENDATION_DATE_FIELD = "recommendationDate";
  public static final String RECOMMENDATION_DATE_VALUE_1 = "2021-12-03T10:15:30+01:00";
  public static final String RECOMMENDATION_DATE_VALUE_2 = "2021-10-12T23:01:30+01:00";

  public static final AlertsExpired ALERTS_EXPIRED_REQUEST =
      AlertsExpired.newBuilder()
          .addAllAlerts(of(ALERT_NAME_1, ALERT_NAME_2))
          .build();

  public static final Map<String, String> MAPPED_ALERT_1 = Map.of(
      DISCRIMINATOR_FIELD, DISCRIMINATOR_1,
      ALERT_NAME_FIELD, ALERT_NAME_1,
      ANALYST_COMMENT_FIELD, COMMENT_CORRECT,
      RECOMMENDATION_DATE_FIELD, RECOMMENDATION_DATE_VALUE_1
  );

  public static final Map<String, String> MAPPED_ALERT_2 = Map.of(
      DISCRIMINATOR_FIELD, DISCRIMINATOR_2,
      ALERT_NAME_FIELD, ALERT_NAME_2,
      ANALYST_COMMENT_FIELD, COMMENT_CORRECT,
      RECOMMENDATION_DATE_FIELD, RECOMMENDATION_DATE_VALUE_2
  );

  public static final Map<String, String> MAPPED_MATCH_1 = Map.of(
      DISCRIMINATOR_FIELD, DISCRIMINATOR_1,
      ALERT_NAME_FIELD, ALERT_NAME_1,
      ANALYST_COMMENT_FIELD, COMMENT_CORRECT
  );

  public static final Match MATCH_1 = Match.newBuilder()
      .setDiscriminator(MATCH_DISCRIMINATOR_1)
      .setName(MATCH_NAME_1)
      .setPayload(convertMapToPayload(MAPPED_MATCH_1))
      .build();

  public static final Alert ALERT_1 = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR_1)
      .setName(ALERT_NAME_1)
      .setPayload(convertMapToPayload(MAPPED_ALERT_1))
      .addAllMatches(of(MATCH_1))
      .build();

  public static final Alert ALERT_2 = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR_2)
      .setName(ALERT_NAME_2)
      .setPayload(convertMapToPayload(MAPPED_ALERT_2))
      .build();

  public static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_1 =
      ProductionDataIndexRequest.newBuilder()
          .setRequestId(randomUUID().toString())
          .addAllAlerts(of(ALERT_1, ALERT_2))
          .build();

  private static Builder convertMapToPayload(Map<String, String> payload) {
    Builder builder = Struct.newBuilder();
    Map<String, Value> convertedMap = new HashMap<>();
    for (String key : payload.keySet()) {
      convertedMap.put(key, toValue(payload.get(key)));
    }
    return builder.putAllFields(convertedMap);
  }

  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}
