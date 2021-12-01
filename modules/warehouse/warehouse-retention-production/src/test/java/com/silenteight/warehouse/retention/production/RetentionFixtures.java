package com.silenteight.warehouse.retention.production;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_NAME;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_PREFIX;
import static java.util.List.of;
import static java.util.UUID.randomUUID;

public class RetentionFixtures {

  public static final String RECOMMENDATION_COMMENT = "recommendation_comment";
  public static final String RECOMMENDATION_COMMENT_PREFIXED = "alert_recommendation_comment";
  public static final String ANALYST_COMMENT = "analyst_comment";
  public static final String ANALYST_COMMENT_PREFIXED = "alert_analyst_comment";
  public static final String COMMENT_TP = "true positive";
  public static final String COMMENT_CORRECT = "correct resolved";

  public static final String PRODUCTION_ALERT_ELASTIC_WRITE_INDEX_NAME =
      "itest_production_alert.2021-04-15";
  public static final String PRODUCTION_MATCH_ELASTIC_WRITE_INDEX_NAME =
      "itest_production_match.*";
  public static final String PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME = "itest_production_alert";
  public static final String PRODUCTION_MATCH_ELASTIC_READ_ALIAS_NAME = "itest_production_match";
  public static final String ALERT_ELASTIC_TEMPLATE_NAME = "alert_template";
  public static final String MATCH_ELASTIC_TEMPLATE_NAME = "match_template";

  public static final String DISCRIMINATOR_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String DISCRIMINATOR_2 = "df3dbd00-4800-4d59-b53e-5bb6c7eb4395";
  public static final String ALERT_NAME_1 = "alerts/457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_NAME_2 = "alerts/168c0428-5b9d-4db7-8e11-86264356433f";
  public static final String MATCH_NAME_1 = "matches/d25641e9-71d6-4705-a90b-5bc3d5303425";
  public static final String MATCH_DISCRIMINATOR_1 = "de26c0cc-a6cc-49c0-a0fb-3f1999ca1e31";
  public static final String SOLUTION_KEY = "solution";
  public static final String SOLUTION_TP = "TRUE_POSITIVE";

  public static final PersonalInformationExpired PERSONAL_INFORMATION_EXPIRED_REQUEST =
      PersonalInformationExpired.newBuilder()
          .addAllAlerts(of(ALERT_NAME_1))
          .build();

  public static final AlertsExpired ALERTS_EXPIRED_REQUEST =
      AlertsExpired.newBuilder()
          .addAllAlerts(of(ALERT_NAME_1, ALERT_NAME_2))
          .build();

  public static final Map<String, String> MAPPED_ALERT_1 = Map.of(
      DISCRIMINATOR, DISCRIMINATOR_1,
      ALERT_NAME, ALERT_NAME_1,
      RECOMMENDATION_COMMENT, COMMENT_TP,
      ANALYST_COMMENT, COMMENT_CORRECT
  );

  public static final Map<String, String> MAPPED_ALERT_2 = Map.of(
      DISCRIMINATOR, DISCRIMINATOR_2,
      ALERT_NAME, ALERT_NAME_2,
      RECOMMENDATION_COMMENT, COMMENT_TP,
      ANALYST_COMMENT, COMMENT_CORRECT
  );

  public static final Map<String, String> MAPPED_MATCH_1 = Map.of(
      DISCRIMINATOR, DISCRIMINATOR_1,
      ALERT_NAME, ALERT_NAME_1,
      RECOMMENDATION_COMMENT, COMMENT_TP,
      ANALYST_COMMENT, COMMENT_CORRECT,
      MATCH_PREFIX + SOLUTION_KEY, SOLUTION_TP,
      MATCH_DISCRIMINATOR, MATCH_DISCRIMINATOR_1,
      MATCH_NAME, MATCH_NAME_1
  );

  public static final Match MATCH_1 = Match.newBuilder()
      .setDiscriminator(MATCH_DISCRIMINATOR_1)
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
          .addAllAlerts(of(ALERT_1))
          .build();

  public static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_2 =
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

  @NotNull
  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}
