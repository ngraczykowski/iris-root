package com.silenteight.hsbc.bridge.recommendation

import spock.lang.Specification

import static java.util.Optional.empty
import static java.util.Optional.of

class RecommendationFacadeSpec extends Specification {

  def repository = Mock(RecommendationRepository)
  def underTest = new RecommendationFacade(repository)

  def 'should return recommendation belongs to an alert'() {
    given:
    def alert = 'alert/1'

    when:
    def result = underTest.getRecommendation(alert)

    then:
    with(result) {
      alert == alert
      recommendationComment == 'Comment'
      recommendedAction == 'FP'
    }
    1 * repository.findByAlert(alert) >> of(new RecommendationEntity(
        recommendedAction: 'FP',
        recommendationComment: 'Comment',
        alert: alert,
        name: 'recommendation'
    ))
  }

  def 'should throw exception when recommendation for an alert has not been found'() {
    given:
    def alert = 'alert/1'

    when:
    underTest.getRecommendation(alert)

    then:
    thrown(RecommendationNotFoundException)
    1 * repository.findByAlert(alert) >> empty()
  }
}
