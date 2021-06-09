package com.silenteight.hsbc.bridge.recommendation

import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest

import spock.lang.Specification

import static java.util.Optional.empty
import static java.util.Optional.of

class GetRecommendationUseCaseSpec extends Specification {

  def mapper = Mock(RecommendationMapper)
  def repository = Mock(RecommendationRepository)
  def underTest = new GetRecommendationUseCase(mapper, repository)

  def someAlert = 'alert/1'
  def someExtendedAttribute = 'SAN'

  def 'should return recommendation belongs to an alert'() {
    given:
    var request = Mock(GetRecommendationRequest) {
      getAlert() >> someAlert
      getExtendedAttribute5() >> someExtendedAttribute
    }

    when:
    def result = underTest.getRecommendation(request)

    then:
    with(result) {
      alert == someAlert
      recommendationComment == 'Comment'
      recommendedAction == 'Level 1 Review'
    }
    1 * repository.findByAlert(request.getAlert()) >> of(
        new RecommendationEntity(
            recommendedAction: 'FP',
            recommendationComment: 'Comment',
            alert: request.getAlert(),
            name: 'recommendation'
        ))
    1 * mapper.getRecommendationValue('FP', request.getExtendedAttribute5()) >> 'Level 1 Review'
  }

  def 'should throw exception when recommendation for an alert has not been found'() {
    given:
    var request = Mock(GetRecommendationRequest) {
      getAlert() >> someAlert
      getExtendedAttribute5() >> someExtendedAttribute
    }

    when:
    underTest.getRecommendation(request)

    then:
    thrown(RecommendationNotFoundException)
    1 * repository.findByAlert(request.getAlert()) >> empty()
    0 * mapper.getRecommendationValue(_ as String, _ as String)
  }
}
