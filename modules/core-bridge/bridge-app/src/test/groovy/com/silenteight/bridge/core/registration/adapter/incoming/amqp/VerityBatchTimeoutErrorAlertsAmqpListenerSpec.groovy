package com.silenteight.bridge.core.registration.adapter.incoming.amqp

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout

import spock.lang.Specification
import spock.lang.Subject

class VerityBatchTimeoutErrorAlertsAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new VerityBatchTimeoutErrorAlertsAmqpListener(registrationFacade)

  def "should call registration facade using command"() {
    given:
    def message = MessageVerifyBatchTimeout.newBuilder()
        .setBatchId(Fixtures.BATCH_ID)
        .build()
    def expectedCommand = new VerifyBatchTimeoutCommand(message.getBatchId())

    when:
    underTest.verifyBatchTimeoutAlerts(message)

    then:
    1 * registrationFacade.verifyBatchTimeoutForAllErroneousAlerts(expectedCommand)
  }
}
