package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.recommendation.api.library.v1.*
import com.silenteight.recommendation.api.library.v1.AlertOut.AlertStatus
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import java.time.OffsetDateTime

class GrpcAdapterFixtures {

  static String ANALYSIS_NAME = 'analysisName'
  static List<String> ALERT_IDS = ['alertId1', 'alertId2']

  static String MATCH_ID = 'matchId'
  static String MATCH_NAME = 'matchName'
  static String MATCH_RECOMMENDED_ACTION = 'matchRecommendedAction'
  static String MATCH_RECOMMENDATION_COMMENT = 'matchRecommendationComment'
  static String STEP_ID = 'stepId'
  static String FV_SIGNATURE = 'fvSignature'
  static String FEATURE_KEY = 'featureKey'
  static String FEATURE_VALUE = 'featureValue'
  static Map<String, String> FEATURES = [featureKey: FEATURE_VALUE] as Map

  static String ALERT_ID = 'alertId'
  static String ALERT_NAME = 'alertName'
  static String ALERT_METADATA = 'alertMetadata'
  static AlertStatus ALERT_STATUS = AlertStatus.SUCCESS
  static String ERROR_MESSAGE = 'errorMessage'

  static String BATCH_ID = 'batchId'
  static String POLICY_ID = 'policyId'
  static String RECOMMENDATION_NAME = 'name'
  static RecommendedAction RECOMMENDED_ACTION = RecommendedAction.ACTION_INVESTIGATE
  static String RECOMMENDATION_COMMENT = 'recommendationComment'
  static OffsetDateTime RECOMMENDED_AT = OffsetDateTime.now()

  static RecommendationsIn RECOMMENDATIONS_IN = RecommendationsIn.builder()
      .analysisName(ANALYSIS_NAME)
      .alertNames(ALERT_IDS)
      .build()

  static List<MatchOut> MATCHES = [
      MatchOut.builder()
          .id(MATCH_ID)
          .name(MATCH_NAME)
          .recommendedAction(MATCH_RECOMMENDED_ACTION)
          .recommendationComment(MATCH_RECOMMENDATION_COMMENT)
          .stepId(STEP_ID)
          .fvSignature(FV_SIGNATURE)
          .features(FEATURES)
          .build()
  ]

  static AlertOut ALERT = AlertOut.builder()
      .id(ALERT_ID)
      .name(ALERT_NAME)
      .metadata(ALERT_METADATA)
      .status(ALERT_STATUS)
      .errorMessage(ERROR_MESSAGE)
      .build()

  static List<RecommendationOut> RECOMMENDATION_OUT_LIST = [
      RecommendationOut.builder()
          .batchId(BATCH_ID)
          .policyId(POLICY_ID)
          .name(RECOMMENDATION_NAME)
          .recommendedAction(RECOMMENDED_ACTION.toString())
          .recommendationComment(RECOMMENDATION_COMMENT)
          .recommendedAt(RECOMMENDED_AT)
          .alert(ALERT)
          .matches(MATCHES)
          .build()
  ]

  static def TOTAL_PROCESSED_COUNT = 10
  static def TOTAL_UNABLE_TO_PROCESS_COUNT = 1
  static def RECOMMENDED_ALERT_COUNT = 10
  static def TRUE_POSITIVE_COUNT = 6
  static def FALSE_POSITIVE_COUNT = 2
  static def MANUAL_INVESTIGATION_COUNT = 2
  static def ERROR_COUNT = 1

  static def STATISTICS_OUT = StatisticsOut.builder()
      .totalProcessedCount(TOTAL_PROCESSED_COUNT.intValue())
      .totalUnableToProcessCount(TOTAL_UNABLE_TO_PROCESS_COUNT.intValue())
      .recommendedAlertsCount(RECOMMENDED_ALERT_COUNT.intValue())
      .recommendationsStatistics(
          RecommendationsStatisticsOut.builder()
              .truePositiveCount(TRUE_POSITIVE_COUNT.intValue())
              .falsePositiveCount(FALSE_POSITIVE_COUNT.intValue())
              .manualInvestigationCount(MANUAL_INVESTIGATION_COUNT.intValue())
              .errorCount(ERROR_COUNT.intValue())
              .build())
      .build()

  static def RECOMMENDATIONS_OUT = RecommendationsOut.builder()
      .recommendations(RECOMMENDATION_OUT_LIST)
      .statistics(STATISTICS_OUT)
      .build()
}
