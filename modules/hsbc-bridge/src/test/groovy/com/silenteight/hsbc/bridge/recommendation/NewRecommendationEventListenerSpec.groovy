package com.silenteight.hsbc.bridge.recommendation

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class NewRecommendationEventListenerSpec extends Specification {

  def eventPublisher = Mock(ApplicationEventPublisher)
  def repository = Mock(RecommendationRepository)
  def underTest = new NewRecommendationEventListener(eventPublisher, repository)

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
    1 * eventPublisher.publishEvent({AlertRecommendationReadyEvent e -> e.alertName == 'alert'})
  }
}
