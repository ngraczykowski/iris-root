package com.silenteight.bridge.core.recommendation.adapter.incoming

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsMapperSpec extends Specification {

  @Subject
  def underTest = new RecommendationsMapper()

  def 'should map to RecommendationWithMetadata'() {
    given:
    def analysisNameInput = Fixtures.ANALYSIS_NAME
    def recommendationInfo = Fixtures.RECOMMENDATION_INFO

    when:
    def result = underTest.toRecommendationWithMetadata(recommendationInfo, analysisNameInput)

    then:
    with(result) {
      name() == Fixtures.RECOMMENDATION_NAME
      alertName() == Fixtures.ALERT
      analysisName() == Fixtures.ANALYSIS_NAME
      recommendedAction() == Fixtures.RECOMMENDED_ACTION
      recommendationComment() == Fixtures.RECOMMENDATION_COMMENT
      recommendedAt() == Fixtures.RECOMMENDATION_CREATE_TIME
      !timeout()

      with(metadata().matchMetadata().first()) {
        match() == Fixtures.MATCH
        solution() == Fixtures.MATCH_METADATA_SOLUTION
        reason() == Fixtures.MATCH_METADATA_REASON
        categories() == Fixtures.MATCH_METADATA_CATEGORIES

        with(features().get(Fixtures.MATCH_METADATA_FEATURES_KEY)) {
          agentConfig() == Fixtures.AGENT_CONFIG
          solution() == Fixtures.FEATURE_SOLUTION
          reason() == Fixtures.FEATURE_METADATA_REASON
        }
      }
    }
  }
}
