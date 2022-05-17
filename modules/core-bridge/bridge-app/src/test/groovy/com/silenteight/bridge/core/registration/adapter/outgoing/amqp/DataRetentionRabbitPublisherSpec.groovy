package com.silenteight.bridge.core.registration.adapter.outgoing.amqp

import com.silenteight.bridge.core.registration.domain.model.AlertToRetention
import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent
import com.silenteight.bridge.core.registration.domain.model.DataRetentionPersonalInformationExpiredEvent
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingDataRetentionProperties
import com.silenteight.dataretention.api.v1.AlertData
import com.silenteight.dataretention.api.v1.AlertsExpired
import com.silenteight.dataretention.api.v1.PersonalInformationExpired

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.bridge.core.registration.adapter.outgoing.amqp.DataRetentionRabbitPublisherSpec.Fixtures.*

class DataRetentionRabbitPublisherSpec extends Specification {

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = PROPERTIES

  @Subject
  def underTest = new DataRetentionRabbitPublisher(properties, rabbitTemplate)

  @Unroll
  def 'should publish #expected.getClass().getSimpleName() with filtered out null alert names'() {
    when:
    underTest.publish(event)

    then:
    1 * rabbitTemplate.convertAndSend(
        properties.exchangeName(), properties.alertsExpiredRoutingKey(), {message ->
      assert message == expected
    })

    where:
    routingKey           | event                       || expected
    ALERTS_ROUTING_KEY   | ALERTS_EXPIRED_EVENT        || ALERTS_EXPIRED_MESSAGE
    PERSONAL_ROUTING_KEY | PERSONAL_INFO_EXPIRED_EVENT || PERSONAL_INFO_MESSAGE
  }

  static class Fixtures {

    static def ALERTS_ROUTING_KEY = 'alertsExpiredRoutingKey'

    static def PERSONAL_ROUTING_KEY = 'alertsExpiredRoutingKey'

    static def PROPERTIES = new AmqpRegistrationOutgoingDataRetentionProperties(
        'exchangeName', PERSONAL_ROUTING_KEY, ALERTS_ROUTING_KEY
    )

    static def ALERTS_TO_RETENTION = [
        AlertToRetention.builder()
            .alertPrimaryId(1l)
            .alertId('1')
            .alertName('alert1')
            .batchId('batch1')
            .build(),
        AlertToRetention.builder()
            .alertPrimaryId(2l)
            .alertId('2')
            .alertName(null)
            .batchId('batch1')
            .build()
    ]

    static def ALERTS_EXPIRED_EVENT = new DataRetentionAlertsExpiredEvent(ALERTS_TO_RETENTION)

    static def PERSONAL_INFO_EXPIRED_EVENT = new DataRetentionPersonalInformationExpiredEvent(
        ALERTS_TO_RETENTION)

    static def EXPECTED_ALERTS_DATA = [
        AlertData.newBuilder()
            .setAlertId('1')
            .setAlertName('alert1')
            .setBatchId('batch1')
            .build(),
        AlertData.newBuilder()
            .setAlertId('2')
            .setAlertName('')
            .setBatchId('batch1')
            .build()
    ]

    static def ALERTS_EXPIRED_MESSAGE = AlertsExpired.newBuilder()
        .addAllAlerts(['alert1'])
        .addAllAlertsData(EXPECTED_ALERTS_DATA)
        .build()

    static def PERSONAL_INFO_MESSAGE = PersonalInformationExpired.newBuilder()
        .addAllAlerts(['alert1'])
        .addAllAlertsData(EXPECTED_ALERTS_DATA)
        .build()
  }
}
