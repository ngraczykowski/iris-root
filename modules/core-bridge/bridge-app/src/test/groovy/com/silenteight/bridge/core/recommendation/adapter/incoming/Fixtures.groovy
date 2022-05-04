package com.silenteight.bridge.core.recommendation.adapter.incoming

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil
import com.silenteight.adjudication.api.v1.Recommendation
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo
import com.silenteight.adjudication.api.v2.RecommendationMetadata
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata

import com.google.protobuf.Struct
import com.google.protobuf.Value

import java.time.OffsetDateTime

class Fixtures {

  static def ANALYSIS_NAME = 'analysisName'
  static def ALERT = 'alerts/<ID of alert>'
  static def MATCH = 'alerts/<ID of alert>/matches/<ID of match>'
  static def RECOMMENDATION_NAME = 'analysis/<ID of analysis>/recommendations/<ID of recommendation>'
  static def AGENT_CONFIG = 'agents/<ID of agent>/versions/<ID of version>/configs/<ID of config>'
  static def FEATURE_SOLUTION = 'featureSolution'
  static def MATCH_METADATA_SOLUTION = 'matchMetadataSolution'
  static def MATCH_METADATA_COMMENT = 'matchMetadataComment'
  static def RECOMMENDATION_METADATA_NAME = 'analysis/<ID of analysis>/recommendations/<ID of recommendation>/metadata'
  static def RECOMMENDED_ACTION = 'recommendedAction'
  static def RECOMMENDATION_COMMENT = 'recommendationComment'
  static def MATCH_METADATA_CATEGORIES = [category: 'someCategory']
  static def RECOMMENDATION_CREATE_TIME = OffsetDateTime.now()
  static def FEATURE_METADATA_REASON_KEY = 'featureMetadataReason'
  static def FEATURE_METADATA_REASON_VALUE = 'featureMetadataReasonValue'
  static def FEATURE_METADATA_REASON = [(FEATURE_METADATA_REASON_KEY): FEATURE_METADATA_REASON_VALUE]
  static def MATCH_METADATA_REASON_KEY = 'matchMetadataReason'
  static def MATCH_METADATA_REASON_VALUE = 'matchMetadataReasonValue'
  static def MATCH_METADATA_REASON = [(MATCH_METADATA_REASON_KEY): MATCH_METADATA_REASON_VALUE]

  static def MATCH_METADATA_REASON_STRUCT = Struct.newBuilder()
      .putAllFields([matchMetadataReason: Value.newBuilder().setStringValue('matchMetadataReasonValue').build()])
      .build()

  static def FEATURE_METADATA_REASON_STRUCT = Struct.newBuilder()
      .putAllFields([featureMetadataReason: Value.newBuilder().setStringValue('featureMetadataReasonValue').build()])
      .build()

  static def FEATURE_METADATA = FeatureMetadata.newBuilder()
      .setAgentConfig(AGENT_CONFIG)
      .setSolution(FEATURE_SOLUTION)
      .setReason(FEATURE_METADATA_REASON_STRUCT)
      .build()

  static def MATCH_METADATA_FEATURES_KEY = 'someFeature'
  static def MATCH_METADATA_FEATURES = [(MATCH_METADATA_FEATURES_KEY): FEATURE_METADATA]

  static def MATCH_METADATA = MatchMetadata.newBuilder()
      .setMatch(MATCH)
      .setSolution(MATCH_METADATA_SOLUTION)
      .setReason(MATCH_METADATA_REASON_STRUCT)
      .putAllCategories(MATCH_METADATA_CATEGORIES)
      .putAllFeatures(MATCH_METADATA_FEATURES)
      .setMatchComment(MATCH_METADATA_COMMENT)
      .build()

  static def RECOMMENDATION_METADATA = RecommendationMetadata.newBuilder()
      .setName(RECOMMENDATION_METADATA_NAME)
      .setAlert(ALERT)
      .addAllMatches([MATCH_METADATA])
      .build()

  static def RECOMMENDATION = Recommendation.newBuilder()
      .setName(RECOMMENDATION_NAME)
      .setAlert(ALERT)
      .setCreateTime(TimeStampUtil.fromOffsetDateTime(RECOMMENDATION_CREATE_TIME))
      .setRecommendedAction(RECOMMENDED_ACTION)
      .setRecommendationComment(RECOMMENDATION_COMMENT)
      .build()

  static def RECOMMENDATION_INFO = RecommendationInfo.newBuilder()
      .setRecommendation(RECOMMENDATION_NAME)
      .setAlert(ALERT)
      .setValue(RECOMMENDATION)
      .setMetadata(RECOMMENDATION_METADATA)
      .build()
}
