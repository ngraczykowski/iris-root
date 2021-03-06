package com.silenteight.hsbc.bridge.recommendation

import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import spock.lang.Specification

class GetRecommendationUseCaseSpec extends Specification {

  def mapper = Mock(RecommendationMapper)
  def repository = Mock(RecommendationRepository)
  def objectMapper = Mock(ObjectMapper)
  def underTest = new GetRecommendationUseCase(mapper, repository, objectMapper)

  def someAlert = 'alert/1'
  def someExtendedAttribute = 'SAN'
  def someObjectNode = Mock(ObjectNode)

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
      metadata
    }
    1 * repository.findByAlert(request.getAlert()) >> Optional.of(
        new RecommendationEntity(
            recommendedAction: 'FP',
            recommendationComment: 'Comment',
            alert: request.getAlert(),
            name: 'recommendation',
            metadata: new RecommendationMetadataEntity(1, someObjectNode)
        ))
    1 * mapper.getRecommendationValue('FP', request.getExtendedAttribute5()) >> 'Level 1 Review'
    1 * objectMapper.treeToValue(someObjectNode, RecommendationMetadata.class) >>
        new RecommendationMetadata()
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
    1 * repository.findByAlert(request.getAlert()) >> Optional.empty()
    0 * mapper.getRecommendationValue(_ as String, _ as String)
  }

  def 'should return recommendation for error alert'() {
    given:
    def someAttribute = 'SAN'

    when:
    def result = underTest.getRecommendationForErrorAlert(someAttribute)

    then:
    1 * mapper.getRecommendationValue(null, someAttribute) >> 'Level 1 Review'
    result == 'Level 1 Review'
  }
}
