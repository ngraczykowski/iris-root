package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil
import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts
import com.silenteight.proto.recommendation.api.v1.Alert
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus
import com.silenteight.proto.recommendation.api.v1.Match
import com.silenteight.proto.recommendation.api.v1.Recommendation
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse

import com.google.protobuf.Struct
import com.google.protobuf.Value

import java.time.OffsetDateTime
import java.time.ZoneOffset

class RecommendationFixtures {

  static def POLICY_NAME = FixturesMatchMetaData.REASON_POLICY
  static def ALERT_NAME = 'alertName'
  static def ANALYSIS_NAME = 'analysisName'
  static def METADATA = 'batchMetadata'
  static def RECOMMENDATION_NAME = 'recommendation_name'
  static def RECOMMENDATION_ACTION = 'recommendation_action'
  static def RECOMMENDATION_COMMENT = 'recommendation_comment'
  static def ERROR_DESCRIPTION = 'error occurred'

  static def ERROR_ALERT = AlertWithMatches.builder()
      .id(Fixtures.ALERT_ID)
      .name('')
      .status(com.silenteight.bridge.core.registration.domain.model.AlertStatus.ERROR)
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

  static def BATCH_WITH_ALERTS = new BatchWithAlerts(Fixtures.BATCH_ID, POLICY_NAME, ALERTS)
  static def BATCH_WITH_ERROR_ALERT = new BatchWithAlerts(
      Fixtures.BATCH_ID, POLICY_NAME, [ERROR_ALERT])

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

  static def ALERT_RECOMMENDATION = Alert
      .newBuilder()
      .setId(Fixtures.ALERT_ID)
      .setStatus(AlertStatus.SUCCESS)
      .setMetadata(METADATA)
      .setErrorMessage('')
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
      .setRecommendationComment('')
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
      .build()

  static def SECOND_MATCH_RECOMMENDATION = Match
      .newBuilder()
      .setId(FixturesMatchMetaData.SECOND_METADATA_MATCH_ID)
      .setRecommendedAction(FixturesMatchMetaData.SECOND_METADATA_SOLUTION)
      .setRecommendationComment('')
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

  static def RECOMMENDATION_RESPONSE = RecommendationsResponse.newBuilder()
      .addAllRecommendations(List.of(RECOMMENDATION))
      .build()

  static def ERRONEOUS_RECOMMENDATION_RESPONSE = RecommendationsResponse.newBuilder()
      .addAllRecommendations(List.of(ERRONEOUS_RECOMMENDATION))
      .build()
}
