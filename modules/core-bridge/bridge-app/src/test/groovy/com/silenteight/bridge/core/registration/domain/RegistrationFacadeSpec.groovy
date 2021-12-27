package com.silenteight.bridge.core.registration.domain


import com.silenteight.bridge.core.registration.domain.model.BatchId

import spock.lang.Specification
import spock.lang.Subject

class RegistrationFacadeSpec extends Specification {

  def batchService = Mock(BatchService)

  @Subject
  def underTest = new RegistrationFacade(batchService)

  def "should call register batch method"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25)

    and:
    batchService.register(registerBatchCommand) >> new BatchId(batchId)

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId
  }

  def "should call notify batch error method"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = "error occurred"
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription)

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchService.notifyBatchError(notifyBatchErrorCommand)
  }
}
