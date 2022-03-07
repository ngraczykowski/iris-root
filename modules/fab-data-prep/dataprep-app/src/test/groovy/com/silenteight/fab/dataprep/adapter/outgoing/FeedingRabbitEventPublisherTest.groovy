package com.silenteight.fab.dataprep.adapter.outgoing

import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.FedMatch
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status
import com.silenteight.fab.dataprep.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class FeedingRabbitEventPublisherTest extends Specification {

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
        .setBatchId(event.getBatchId())
        .setAlertId(event.getAlertId())
        .setAlertErrorDescription(event.getErrorDescription().getDescription())
        .setFeedingStatus(FeedingStatus.SUCCESS)
        .addAllFedMatches(
            [com.silenteight.proto.registration.api.v1.FedMatch.newBuilder()
                 .setMatchId(event.getFedMatches().first().getMatchId())
                 .build()])
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(properties.getExchangeName(), '', message)
  }
}
