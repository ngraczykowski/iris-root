package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.RecommendationsStatisticsService.RecommendedAction
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.bridge.core.recommendation.domain.RecommendationsStatisticsService.RecommendedAction.*

class RecommendationsStatisticsServiceTest extends Specification {

  @Subject
  def underTest = new RecommendationsStatisticsService()

  def 'should create recommendations statistics'() {
    given:
    def recommendations = [
        [createRecommendationWithAction(ACTION_INVESTIGATE)] * 1,
        [createRecommendationWithAction(ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE)] * 2,
        [createRecommendationWithAction(ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE)] * 3,
        [createRecommendationWithAction(ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE)] * 4,
        [createRecommendationWithAction(ACTION_FALSE_POSITIVE)] * 5,
        [createRecommendationWithAction(ACTION_POTENTIAL_TRUE_POSITIVE)] * 6,
    ].flatten() as List<RecommendationWithMetadata>

    when:
    def result = underTest.createRecommendationsStatistics(recommendations)

    then:
    with(result) {
      truePositiveCount() == 6
      falsePositiveCount() == 5
      manualInvestigationCount() == 10
    }
  }

  private static def createRecommendationWithAction(RecommendedAction action) {
    RecommendationWithMetadata.builder()
        .recommendedAction(action.name())
        .build()
  }
}
