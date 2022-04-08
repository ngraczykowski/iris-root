package com.silenteight.bridge.core.registration.adapter.incoming.amqp

import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsDeliveredCommand
import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsDeliveredRabbitAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new RecommendationsDeliveredRabbitAmqpListener(registrationFacade)

  def 'should call registrationFacade with mapped dto'() {
    given:
    def message = RecommendationsDelivered.newBuilder()
        .setBatchId('batchId')
        .setAnalysisName('analysisName')
        .addAllAlertNames(['firstAlertName', 'secondAlertName'])
        .build()
    def command = MarkAlertsAsDeliveredCommand.builder()
        .batchId(message.batchId)
        .analysisName(message.analysisName)
        .alertNames(message.getAlertNamesList())
        .build()

    when:
    underTest.recommendationDelivered(message)

    then:
    1 * registrationFacade.markAlertsAsDelivered(command)
  }
}
