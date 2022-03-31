package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsRecommendedCommand
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsStoredRabbitAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new RecommendationsStoredRabbitAmqpListener(registrationFacade)

  def 'should call alertService and batchService with mapped dto'() {
    given:
    def message = RecommendationsStored.newBuilder()
        .setAnalysisName('analysisName')
        .addAllAlertNames(['firstAlertName', 'secondAlertName'])
        .build()

    when:
    underTest.recommendationsStored(message)

    then:
    1 * registrationFacade.markAlertsAsRecommended(_) >> {MarkAlertsAsRecommendedCommand command ->
      assert command.analysisName == message.analysisName
      assert command.alertNames.size() == message.getAlertNamesCount()
      assert command.alertNames[0] == message.getAlertNames(0)
      assert command.alertNames[1] == message.getAlertNames(1)
    }
  }
}
