package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted
import com.silenteight.bridge.core.registration.domain.model.BatchError

import spock.lang.Specification
import spock.lang.Subject

class RabbitEventMapperSpec extends Specification {

  @Subject
  def underTest = new RabbitEventMapper()

  def 'should map to message batch completed'() {
    given:
    def batchCompleted = BatchCompleted.builder()
        .id('batchId')
        .analysisId('analysisName')
        .alertIds(['firstAlertName', 'secondAlertName'])
        .batchMetadata('batchMetadata')
        .build()

    when:
    def result = underTest.toMessageBatchCompleted(batchCompleted)

    then:
    with(result) {
      batchId == 'batchId'
      analysisId == 'analysisName'
      alertIdsList.first() == 'firstAlertName'
      batchMetadata == 'batchMetadata'
    }
  }

  def 'should map to message batch error'() {
    given:
    def batchError = BatchError.builder()
        .id('batchId')
        .batchMetadata('batchMetadata')
        .errorDescription('Failed to register batch in Core Bridge Registration')
        .build()

    when:
    def result = underTest.toMessageBatchError(batchError)

    then:
    with(result) {
      batchId == 'batchId'
      batchMetadata == 'batchMetadata'
      errorDescription == 'Failed to register batch in Core Bridge Registration'
    }
  }
}
