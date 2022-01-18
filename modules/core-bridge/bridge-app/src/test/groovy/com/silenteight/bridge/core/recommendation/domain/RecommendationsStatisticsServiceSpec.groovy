package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.model.RecommendedActionStatistics
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsStatisticsServiceSpec extends Specification {

  def recommendationRepository = Mock(RecommendationRepository)

  @Subject
  def underTest = new RecommendationsStatisticsService(recommendationRepository)

  def 'should create recommendations statistics'() {
    given:
    def analysisName = 'analysisName'
    def recommendedActionStatistics = new RecommendedActionStatistics(
        Map.of(
            "ACTION_INVESTIGATE", 1,
            "ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE", 2,
            "ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE", 3,
            "ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE", 4,
            "ACTION_FALSE_POSITIVE", 5,
            "ACTION_POTENTIAL_TRUE_POSITIVE", 6))

    and:
    1 * recommendationRepository.countRecommendationsByActionForAnalysisName(analysisName) >>
        recommendedActionStatistics

    when:
    def result = underTest.createRecommendationsStatistics(analysisName)

    then:
    with(result) {
      truePositiveCount() == 6
      falsePositiveCount() == 5
      manualInvestigationCount() == 10
    }
  }
}
