package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.registration.domain.MarkAlertsAsRecommendedCommand
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsReceivedRabbitAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new RecommendationsReceivedRabbitAmqpListener(registrationFacade)

  def 'should call alertService and batchService with mapped dto'() {
    given:
    def message = RecommendationsReceived.newBuilder()
        .setAnalysisId('analysisName')
        .addAllAlertIds(['firstAlertName', 'secondAlertName'])
        .build()

    when:
    underTest.recommendationReceived(message)

    then:
    1 * registrationFacade.markAlertsAsRecommended(_) >> {MarkAlertsAsRecommendedCommand command ->
      command.analysisName == message.analysisId
      command.alertNames.size() == message.getAlertIdsCount()
      command.alertNames[0] == message.getAlertIds(0)
      command.alertNames[1] == message.getAlertIds(1)
    }
  }
}
