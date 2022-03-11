package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.registration.domain.command.MarkBatchAsDeliveredCommand
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
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
        .addAllAlertIds(['firstAlertId', 'secondAlertId'])
        .build()

    when:
    underTest.recommendationDelivered(message)

    then:
    1 * registrationFacade.markBatchAsDelivered(_) >> {MarkBatchAsDeliveredCommand command ->
      assert command.batchId() == message.batchId
    }
  }
}
