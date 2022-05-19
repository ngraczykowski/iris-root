package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil
import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand
import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics.RecommendationsStats
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto.MatchDto
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts
import com.silenteight.proto.recommendation.api.v1.*
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus

import com.google.protobuf.Struct
import com.google.protobuf.Value

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.stream.Stream

class RecommendationFixtures {

  static def POLICY_NAME = FixturesMatchMetaData.REASON_POLICY
  static def ALERT_NAME = 'alertName'
  static def ANALYSIS_NAME = 'analysisName'
  static def METADATA = 'batchMetadata'
  static def RECOMMENDATION_NAME = 'recommendation_name'
  static def RECOMMENDATION_ACTION = 'recommendation_action'
  static def RECOMMENDATION_COMMENT = 'recommendation_comment'
  static def ERROR_DESCRIPTION = 'error occurred'
  static def FIRST_METADATA_MATCH_NAME = 'alerts/1/matches/1'
  static def SECOND_METADATA_MATCH_NAME = 'alerts/1/matches/2'

  static def READY_RECOMMENDATIONS_COMMAND = new ProceedReadyRecommendationsCommand(
      [RECOMMENDATION_WITH_METADATA])

  static def TIMED_OUT_RECOMMENDATIONS_COMMAND = new ProceedBatchTimeoutCommand(
      ANALYSIS_NAME, [ALERT_NAME])

  static def GET_RECOMMENDATIONS_RESPONSE_COMMAND = new GetRecommendationCommand(ANALYSIS_NAME, [])

  static def GET_RECOMMENDATIONS_BY_ALERT_NAMES_RESPONSE_COMMAND = new GetRecommendationCommand(
      ANALYSIS_NAME, [ALERT_NAME])

  static def ERROR_ALERT_DTO = AlertWithMatchesDto.builder()
      .id(Fixtures.ALERT_ID)
      .name('')
      .status(BatchWithAlertsDto.AlertStatus.ERROR)
      .metadata(METADATA)
      .errorDescription(ERROR_DESCRIPTION)
      .matches([])
      .build()

  static def ALERTS = [
      AlertWithMatches.builder()
          .id(Fixtures.ALERT_ID)
          .name(ALERT_NAME)
          .status(com.silenteight.bridge.core.registration.domain.model.AlertStatus.RECOMMENDED)
          .metadata(METADATA)
          .matches(
              [
                  new AlertWithMatches.Match(
                      FixturesMatchMetaData.FIRST_METADATA_MATCH_ID,
                      FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME),
                  new AlertWithMatches.Match(
                      FixturesMatchMetaData.SECOND_METADATA_MATCH_ID,
                      FixturesMatchMetaData.SECOND_METADATA_MATCH_NAME)
              ])
          .build()
  ]

  static def ALERTS_DTO = [
      AlertWithMatchesDto.builder()
          .id(Fixtures.ALERT_ID)
          .name(ALERT_NAME)
          .status(BatchWithAlertsDto.AlertStatus.RECOMMENDED)
          .metadata(METADATA)
          .matches(
              [
                  new MatchDto(
                      FixturesMatchMetaData.FIRST_METADATA_MATCH_ID,
                      FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME),
                  new MatchDto(
                      FixturesMatchMetaData.SECOND_METADATA_MATCH_ID,
                      FixturesMatchMetaData.SECOND_METADATA_MATCH_NAME)
              ])
          .build()
  ]

  static def BATCH_WITH_ALERTS = new BatchWithAlerts(Fixtures.BATCH_ID, POLICY_NAME, ALERTS)
  static def BATCH_WITH_ALERTS_DTO = new BatchWithAlertsDto(
      Fixtures.BATCH_ID, POLICY_NAME, ALERTS_DTO)
  static def BATCH_WITH_ERROR_ALERT_DTO = new BatchWithAlertsDto(
      Fixtures.BATCH_ID, POLICY_NAME, [ERROR_ALERT_DTO])

  static def RECOMMENDATION_RECOMMENDED_AT = OffsetDateTime
      .of(2022, 1, 18, 14, 30, 30, 0, ZoneOffset.UTC)
  static def RECOMMENDATION_METADATA = new RecommendationMetadata(
      [
          FixturesMatchMetaData.FIRST_MATCH_METADATA,
          FixturesMatchMetaData.SECOND_MATCH_METADATA
      ])

  static def RECOMMENDATION_WITH_METADATA = RecommendationWithMetadata.builder()
      .name(RECOMMENDATION_NAME)
      .analysisName(ANALYSIS_NAME)
      .alertName(ALERT_NAME)
      .recommendedAction(RECOMMENDATION_ACTION)
      .recommendationComment(RECOMMENDATION_COMMENT)
      .recommendedAt(RECOMMENDATION_RECOMMENDED_AT)
      .metadata(RECOMMENDATION_METADATA)
      .build()

  static def RECOMMENDATION_WITHOUT_METADATA = RecommendationWithMetadata.builder()
      .name(RECOMMENDATION_NAME)
      .analysisName(ANALYSIS_NAME)
      .alertName(ALERT_NAME)
      .recommendedAction(RECOMMENDATION_ACTION)
      .recommendationComment(RECOMMENDATION_COMMENT)
      .recommendedAt(RECOMMENDATION_RECOMMENDED_AT)
      .metadata(null)
      .build()

  static def ALERT_RECOMMENDATION = Alert
      .newBuilder()
      .setId(Fixtures.ALERT_ID)
      .setStatus(AlertStatus.SUCCESS)
      .setMetadata(METADATA)
      .setErrorMessage('')
      .setName(ALERT_NAME)
      .build()

  static def ERRONEOUS_ALERT_RECOMMENDATION = Alert
      .newBuilder()
      .setId(Fixtures.ALERT_ID)
      .setStatus(AlertStatus.FAILURE)
      .setMetadata(METADATA)
      .setErrorMessage(ERROR_DESCRIPTION)
      .build()

  static def FIRST_MATCH_RECOMMENDATION = Match
      .newBuilder()
      .setId(FixturesMatchMetaData.FIRST_METADATA_MATCH_ID)
      .setRecommendedAction(FixturesMatchMetaData.FIRST_METADATA_SOLUTION)
      .setRecommendationComment(FixturesMatchMetaData.FIRST_METADATA_MATCH_COMMENT)
      .setStepId(FixturesMatchMetaData.FIRST_METADATA_REASON_STEP)
      .setFvSignature(FixturesMatchMetaData.FIRST_METADATA_REASON_SIGNATURE)
      .setFeatures(
          Struct.newBuilder()
              .putFields(
                  FixturesMatchMetaData.FIRST_METADATA_FEATURE_NAME_WITHOUT_PREFIX,
                  Value.newBuilder()
                      .setStringValue(FixturesMatchMetaData.FIRST_METADATA_FEATURE_SOLUTION)
                      .build())
              .build()
      )
      .setName(FIRST_METADATA_MATCH_NAME)
      .build()

  static def SECOND_MATCH_RECOMMENDATION = Match
      .newBuilder()
      .setId(FixturesMatchMetaData.SECOND_METADATA_MATCH_ID)
      .setRecommendedAction(FixturesMatchMetaData.SECOND_METADATA_SOLUTION)
      .setRecommendationComment(FixturesMatchMetaData.SECOND_METADATA_MATCH_COMMENT)
      .setStepId(FixturesMatchMetaData.SECOND_METADATA_REASON_STEP)
      .setFvSignature(FixturesMatchMetaData.SECOND_METADATA_REASON_SIGNATURE)
      .setFeatures(
          Struct.newBuilder()
              .putFields(
                  FixturesMatchMetaData.SECOND_METADATA_FEATURE_NAME_WITHOUT_PREFIX,
                  Value.newBuilder()
                      .setStringValue(FixturesMatchMetaData.SECOND_METADATA_FEATURE_SOLUTION)
                      .build())
              .build())
      .setName(SECOND_METADATA_MATCH_NAME)
      .build()

  static def RECOMMENDATION = Recommendation.newBuilder()
      .setBatchId(Fixtures.BATCH_ID)
      .setName(RECOMMENDATION_NAME)
      .setRecommendedAction(RECOMMENDATION_ACTION)
      .setRecommendationComment(RECOMMENDATION_COMMENT)
      .setPolicyId(POLICY_NAME)
      .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(RECOMMENDATION_RECOMMENDED_AT))
      .setAlert(ALERT_RECOMMENDATION)
      .addAllMatches([FIRST_MATCH_RECOMMENDATION, SECOND_MATCH_RECOMMENDATION])
      .build()

  static def RECOMMENDATION_WITHOUT_MATCHES = Recommendation.newBuilder()
      .setBatchId(Fixtures.BATCH_ID)
      .setName(RECOMMENDATION_NAME)
      .setRecommendedAction(RECOMMENDATION_ACTION)
      .setRecommendationComment(RECOMMENDATION_COMMENT)
      .setPolicyId(POLICY_NAME)
      .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(RECOMMENDATION_RECOMMENDED_AT))
      .setAlert(ALERT_RECOMMENDATION)
      .addAllMatches([])
      .build()

  static def ERRONEOUS_RECOMMENDATION = Recommendation.newBuilder()
      .setBatchId(Fixtures.BATCH_ID)
      .setName("")
      .setRecommendedAction("")
      .setRecommendationComment("")
      .setPolicyId(POLICY_NAME)
      .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(RECOMMENDATION_RECOMMENDED_AT))
      .setAlert(ERRONEOUS_ALERT_RECOMMENDATION)
      .addAllMatches([])
      .build()

  static def RECOMMENDATION_STATS = new RecommendationsStats(1, 2, 3, 4)
  static def BATCH_STATISTICS = new BatchStatistics(1, 2, 3, RECOMMENDATION_STATS)

  static def STATISTICS = Statistics.newBuilder()
      .setTotalProcessedCount(BATCH_STATISTICS.totalProcessedCount())
      .setRecommendedAlertsCount(BATCH_STATISTICS.recommendedAlertsCount())
      .setTotalUnableToProcessCount(BATCH_STATISTICS.totalUnableToProcessCount())
      .setRecommendationsStatistics(
          RecommendationsStatistics.newBuilder()
              .setTruePositiveCount(RECOMMENDATION_STATS.truePositiveCount())
              .setFalsePositiveCount(RECOMMENDATION_STATS.falsePositiveCount())
              .setManualInvestigationCount(RECOMMENDATION_STATS.manualInvestigationCount())
              .setErrorCount(RECOMMENDATION_STATS.errorCount())
              .build())
      .build()

  static def RECOMMENDATIONS_RESPONSE = RecommendationsResponse.newBuilder()
      .addAllRecommendations(List.of(RECOMMENDATION))
      .setStatistics(STATISTICS)
      .build()

  static def RECOMMENDATION_RESPONSE = RecommendationResponse.newBuilder()
      .setRecommendation(RECOMMENDATION)
      .setStatistics(STATISTICS)
      .build()

  static def RECOMMENDATION_WITHOUT_METADATA_RESPONSE = RecommendationsResponse.newBuilder()
      .addAllRecommendations(List.of(RECOMMENDATION_WITHOUT_MATCHES))
      .setStatistics(STATISTICS)
      .build()

  static def ERRONEOUS_RECOMMENDATIONS_RESPONSE = RecommendationsResponse.newBuilder()
      .addAllRecommendations(List.of(ERRONEOUS_RECOMMENDATION))
      .setStatistics(STATISTICS)
      .build()

  static def ERRONEOUS_RECOMMENDATION_RESPONSE = RecommendationResponse.newBuilder()
      .setRecommendation(ERRONEOUS_RECOMMENDATION)
      .setStatistics(STATISTICS)
      .build()

  static def BATCH_ID_WITH_POLICY = new BatchIdWithPolicy(Fixtures.BATCH_ID, POLICY_NAME)

  static def ALERTS_WITHOUT_MATCHES = AlertWithoutMatches.builder()
      .id("1")
      .alertId(Fixtures.ALERT_ID)
      .alertName(ALERT_NAME)
      .alertStatus(AlertWithoutMatches.AlertStatus.RECOMMENDED)
      .metadata(METADATA)
      .build()

  static def ERROR_ALERTS_WITHOUT_MATCHES = AlertWithoutMatches.builder()
      .id("1")
      .alertId(Fixtures.ALERT_ID)
      .errorDescription(ERROR_DESCRIPTION)
      .alertStatus(AlertWithoutMatches.AlertStatus.ERROR)
      .metadata(METADATA)
      .build()


  static def ALERTS_WITHOUT_MATCHES_WITHOUT_NAME = AlertWithoutMatches.builder()
      .id("1")
      .alertId(Fixtures.ALERT_ID)
      .alertName(null)
      .alertStatus(AlertWithoutMatches.AlertStatus.RECOMMENDED)
      .metadata(METADATA)
      .build()

  static def MATCHES_WITH_ALERTS_IDS = List.of(
      MatchWithAlertId.builder()
          .alertId("1")
          .id(FixturesMatchMetaData.FIRST_METADATA_MATCH_ID)
          .name(FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME)
          .build(),
      MatchWithAlertId.builder()
          .alertId("1")
          .id(FixturesMatchMetaData.SECOND_METADATA_MATCH_ID)
          .name(FixturesMatchMetaData.SECOND_METADATA_MATCH_NAME)
          .build()
  )
}
