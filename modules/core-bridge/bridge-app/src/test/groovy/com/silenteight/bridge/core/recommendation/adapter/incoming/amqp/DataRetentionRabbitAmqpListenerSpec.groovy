package com.silenteight.bridge.core.recommendation.adapter.incoming.amqp

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade
import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand
import com.silenteight.dataretention.api.v1.AlertsExpired

import spock.lang.Specification
import spock.lang.Subject

class DataRetentionRabbitAmqpListenerSpec extends Specification {

  static def ALERT_NAMES = ['1', '2', '3']
  static def COMMAND = new ProceedDataRetentionOnRecommendationsCommand(ALERT_NAMES)

  def facade = Mock(RecommendationFacade)

  @Subject
  def underTest = new DataRetentionRabbitAmqpListener(facade)

  def 'should receive AlertsExpired and call facade'() {
    given:
    def msg = AlertsExpired.newBuilder()
        .addAllAlerts(ALERT_NAMES)
        .build()

    when:
    underTest.subscribe(msg)

    then:
    1 * facade.performDataRetention(COMMAND)
  }
}
