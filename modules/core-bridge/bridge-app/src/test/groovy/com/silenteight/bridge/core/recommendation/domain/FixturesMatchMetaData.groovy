package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.FeatureMetadata
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata

class FixturesMatchMetaData {

  static def REASON_POLICY = 'policies/5afc2f12-85c0-4fb3-992e-1552ac843ceb'

  static def FIRST_METADATA_MATCH_ID = 'match_id_1'
  static def FIRST_METADATA_MATCH_NAME = 'alerts/1/matches/1'
  static def FIRST_METADATA_SOLUTION = 'SOLUTION_FALSE_POSITIVE'

  static def FIRST_METADATA_REASON_SIGNATURE = 'J4VGkp1+FaNsaGDtBXgQsWpUYDo='
  static def FIRST_METADATA_REASON_STEP = 'steps/e6ceb774-ab56-4576-b653-1cdceb2d25e7'
  static def FIRST_METADATA_REASON = createReasonMap(
      FIRST_METADATA_REASON_SIGNATURE, REASON_POLICY, FIRST_METADATA_REASON_STEP)

  static def FIRST_METADATA_FEATURE_NAME_WITHOUT_PREFIX = 'name'
  static def FIRST_METADATA_FEATURE_NAME = "features/$FIRST_METADATA_FEATURE_NAME_WITHOUT_PREFIX"
  static def FIRST_METADATA_FEATURE_AGENT_CONFIG = 'agents/name/versions/1.0.0/configs/1'
  static def FIRST_METADATA_FEATURE_SOLUTION = 'EXACT_MATCH'
  static def FIRST_METADATA_FEATURES = createFeaturesMap(
      FIRST_METADATA_FEATURE_NAME, FIRST_METADATA_FEATURE_AGENT_CONFIG,
      FIRST_METADATA_FEATURE_SOLUTION)

  static def FIRST_MATCH_METADATA = MatchMetadata.builder()
      .match(FIRST_METADATA_MATCH_NAME)
      .solution(FIRST_METADATA_SOLUTION)
      .categories(Map.of())
      .reason(FIRST_METADATA_REASON)
      .features(FIRST_METADATA_FEATURES)
      .build()

  static def SECOND_METADATA_MATCH_ID = 'match_id_2'
  static def SECOND_METADATA_MATCH_NAME = 'alerts/1/matches/2'
  static def SECOND_METADATA_SOLUTION = 'SOLUTION_TRUE_POSITIVE'
  static def SECOND_METADATA_REASON_SIGNATURE = 'J4VGkp1+TnNsaGDtBXgQsWpUYDo='
  static def SECOND_METADATA_REASON_STEP = 'steps/03eccb7f-75f7-407d-9bdf-564c6d264cb1'
  static def SECOND_METADATA_REASON = createReasonMap(
      SECOND_METADATA_REASON_SIGNATURE, REASON_POLICY, SECOND_METADATA_REASON_STEP)

  static def SECOND_METADATA_FEATURE_NAME_WITHOUT_PREFIX = 'agent'
  static def SECOND_METADATA_FEATURE_NAME = "features/$SECOND_METADATA_FEATURE_NAME_WITHOUT_PREFIX"
  static def SECOND_METADATA_FEATURE_AGENT_CONFIG = 'agents/name/versions/1.0.0/configs/1'
  static def SECOND_METADATA_FEATURE_SOLUTION = 'NO_MATCH'
  static def SECOND_METADATA_FEATURES = createFeaturesMap(
      SECOND_METADATA_FEATURE_NAME, SECOND_METADATA_FEATURE_AGENT_CONFIG,
      SECOND_METADATA_FEATURE_SOLUTION)

  static def SECOND_MATCH_METADATA = MatchMetadata.builder()
      .match(SECOND_METADATA_MATCH_NAME)
      .solution(SECOND_METADATA_SOLUTION)
      .categories(Map.of())
      .reason(SECOND_METADATA_REASON)
      .features(SECOND_METADATA_FEATURES)
      .build()

  static private Map<String, String> createReasonMap(String signature, String policy, String step) {
    return Map.of(
        'feature_vector_signature', signature,
        'policy', policy,
        'step', step
    )
  }

  static private Map<String, FeatureMetadata> createFeaturesMap(
      String featureName, String agentConfig, String solution) {
    return Map.of(
        featureName, FeatureMetadata.builder()
        .agentConfig(agentConfig)
        .solution(solution)
        .build()
    )
  }
}
