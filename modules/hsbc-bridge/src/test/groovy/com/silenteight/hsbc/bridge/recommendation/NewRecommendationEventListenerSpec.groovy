package com.silenteight.hsbc.bridge.recommendation

import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent

import spock.lang.Specification

class NewRecommendationEventListenerSpec extends Specification {

  def repository = Mock(RecommendationRepository)
  def underTest = new NewRecommendationEventListener(repository)

  def 'should handle new recommendation'() {
    given:
    def event = new NewRecommendationEvent(
        RecommendationDto.builder()
            .alert("alert")
            .name("alerts/1/recommendation/1")
            .recommendedAction("FALSE POSITIVE")
            .recommendationComment("False positive comment")
            .build())

    when:
    underTest.onNewRecommendationEvent(event)

    then:
    1 * repository.save(_ as RecommendationEntity) >> {RecommendationEntity entity -> entity.id = 2}
  }
}
