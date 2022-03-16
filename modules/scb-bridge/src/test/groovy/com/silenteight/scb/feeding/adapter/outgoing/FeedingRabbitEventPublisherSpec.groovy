package com.silenteight.scb.feeding.adapter.outgoing

import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus
import com.silenteight.scb.feeding.domain.model.AlertErrorDescription
import com.silenteight.scb.feeding.domain.model.UdsFedEvent
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.FedMatch
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.Status
import com.silenteight.scb.feeding.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class FeedingRabbitEventPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new AmqpFeedingOutgoingMatchFeatureInputSetFedProperties('test')

  @Subject
  def underTest = new FeedingRabbitEventPublisher(rabbitTemplate, properties)

  def 'should publish MatchFeatureInputSetFed message'() {
    given:
    def event = UdsFedEvent.builder()
        .batchId('batchId')
        .alertId('alertId')
        .errorDescription(AlertErrorDescription.NONE)
        .feedingStatus(Status.SUCCESS)
        .fedMatches([new FedMatch('matchId')])
        .build()

    def message = MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(event.batchId())
        .setAlertId(event.alertId())
        .setAlertErrorDescription(event.errorDescription().getDescription())
        .setFeedingStatus(FeedingStatus.SUCCESS)
        .addAllFedMatches(
            [com.silenteight.proto.registration.api.v1.FedMatch.newBuilder()
                 .setMatchId(event.fedMatches().first().matchId())
                 .build()])
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(properties.exchangeName(), '', message);
  }
}
