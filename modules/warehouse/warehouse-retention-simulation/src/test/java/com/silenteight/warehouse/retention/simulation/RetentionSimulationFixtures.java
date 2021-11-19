package com.silenteight.warehouse.retention.simulation;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants;

import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SIMULATION_ANALYSIS_ID;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.util.List.of;

public class RetentionSimulationFixtures {

  public static final String RECOMMENDATION_COMMENT_PREFIXED = "alert_recommendation_comment";
  public static final String ANALYST_COMMENT_PREFIXED = "alert_analyst_comment";
  public static final String COMMENT_TP = "true positive";
  public static final String COMMENT_CORRECT = "correct resolved";

  public static final String SIMULATION_ELASTIC_INDEX_NAME =
      "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  public static final String DISCRIMINATOR_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_NAME_1 = "alerts/457b1498-e348-4a81-8093-6079c1173010";

  public static final AnalysisExpired ANALYSIS_EXPIRED_REQUEST =
      AnalysisExpired.newBuilder()
          .addAllAnalysis(of(SIMULATION_ANALYSIS_NAME))
          .build();

  public static final Map<String, Object> MAPPED_ALERT_1 = Map.of(
      DISCRIMINATOR, DISCRIMINATOR_1,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      AlertMapperConstants.ALERT_NAME, Values.ALERT_NAME,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK,
      RECOMMENDATION_COMMENT_PREFIXED, COMMENT_TP,
      ANALYST_COMMENT_PREFIXED, COMMENT_CORRECT
  );

  @NotNull
  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}
