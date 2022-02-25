package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout

import spock.lang.Specification
import spock.lang.Subject

class VerifyBatchTimeoutAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new VerifyBatchTimeoutAmqpListener(registrationFacade)

  def "should call registration facade using command"() {
    given:
    def message = MessageVerifyBatchTimeout.newBuilder()
        .setBatchId(Fixtures.BATCH_ID)
        .build()

    when:
    underTest.verifyBatchTimeout(message)

    then:
    1 * registrationFacade.verifyBatchTimeout(new VerifyBatchTimeoutCommand(message.getBatchId()))
  }
}
