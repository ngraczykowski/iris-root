package com.silenteight.fab.dataprep.adapter.outgoing

import com.silenteight.data.api.v2.Alert
import com.silenteight.data.api.v2.Match
import com.silenteight.data.api.v2.ProductionDataIndexRequest
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent

import com.google.protobuf.Struct
import com.google.protobuf.Value
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.adapter.outgoing.LearningRabbitEventPublisher.EXCHANGE_NAME
import static com.silenteight.fab.dataprep.adapter.outgoing.LearningRabbitEventPublisher.ROUTING_KEY
import static com.silenteight.fab.dataprep.domain.Fixtures.ALERT_NAME

class LearningRabbitEventPublisherTest extends Specification {

  private static final String REQUEST_ID = '12345'
  private static final String ACCESS_PERMISSION_TAG = 'PL'
  private static final String ALERT_DISCRIMINATOR = 'alert-discriminator'
  private static final String MATCH_DISCRIMINATOR = 'match-discriminator'
  private static final String ALERT_PAYLOAD_KEY = 'alert-key'
  private static final String ALERT_PAYLOAD_VALUE = 'alert-value'
  private static final String MATCH_PAYLOAD_KEY = 'match-key'
  private static final String MATCH_PAYLOAD_VALUE = 'match-value'
  private static final String MATCH_NAME = 'match-name'

  def rabbitTemplate = Mock(RabbitTemplate)

  @Subject
  def underTest = new LearningRabbitEventPublisher(rabbitTemplate)

  def 'should publish MatchFeatureInputSetFed message'() {
    given:
    def event = WarehouseEvent.builder()
        .requestId(REQUEST_ID)
        .alerts(
            [WarehouseEvent.Alert.builder()
                 .alertName(ALERT_NAME)
                 .accessPermissionTag(ACCESS_PERMISSION_TAG)
                 .discriminator(ALERT_DISCRIMINATOR)
                 .payload([(ALERT_PAYLOAD_KEY): ALERT_PAYLOAD_VALUE])
                 .matches(
                     [WarehouseEvent.Match.builder()
                          .matchName(MATCH_NAME)
                          .discriminator(MATCH_DISCRIMINATOR)
                          .payload([(MATCH_PAYLOAD_KEY): MATCH_PAYLOAD_VALUE])
                          .build()])
                 .build()])
        .build()

    def message = ProductionDataIndexRequest.newBuilder()
        .setRequestId(REQUEST_ID)
        .addAlerts(
            Alert.newBuilder()
                .setName(ALERT_NAME)
                .setDiscriminator(ALERT_DISCRIMINATOR)
                .setAccessPermissionTag(ACCESS_PERMISSION_TAG)
                .setPayload(
                    Struct.newBuilder()
                        .putFields(
                            (ALERT_PAYLOAD_KEY),
                            Value.newBuilder().setStringValue(ALERT_PAYLOAD_VALUE).build()))
                .addMatches(
                    Match.newBuilder()
                        .setName(MATCH_NAME)
                        .setDiscriminator(MATCH_DISCRIMINATOR)
                        .setPayload(
                            Struct.newBuilder()
                                .putFields(
                                    (MATCH_PAYLOAD_KEY),
                                    Value.newBuilder().setStringValue(MATCH_PAYLOAD_VALUE).build()))
                        .build())
                .build())
        .build()

    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message)
  }
}
