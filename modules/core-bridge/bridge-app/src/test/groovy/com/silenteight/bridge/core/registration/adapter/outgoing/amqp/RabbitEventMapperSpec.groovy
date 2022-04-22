package com.silenteight.bridge.core.registration.adapter.outgoing.amqp

import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut
import com.silenteight.bridge.core.registration.domain.model.SimulationBatchCompleted
import com.silenteight.bridge.core.registration.domain.model.SolvingBatchCompleted

import spock.lang.Specification
import spock.lang.Subject

class RabbitEventMapperSpec extends Specification {

  @Subject
  def underTest = new RabbitEventMapper()

  def 'should map from solving batch completed event to message batch completed'() {
    given:
    def solvingBatchCompleted = SolvingBatchCompleted.builder()
        .id('batchId')
        .analysisName('analysisName')
        .batchMetadata('batchMetadata')
        .build()

    when:
    def result = underTest.toMessageBatchCompleted(solvingBatchCompleted)

    then:
    with(result) {
      batchId == 'batchId'
      analysisName == 'analysisName'
      batchMetadata == 'batchMetadata'
    }
  }

  def 'should map from simulation batch completed event to message batch completed'() {
    given:
    def simulationBatchCompleted = SimulationBatchCompleted.builder()
        .id('batchId')
        .analysisName('analysisName')
        .batchMetadata('batchMetadata')
        .build()

    when:
    def result = underTest.toMessageBatchCompleted(simulationBatchCompleted)

    then:
    with(result) {
      batchId == 'batchId'
      analysisName == 'analysisName'
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

  def 'should map to message batch timed out'() {
    given:
    def analysisName = 'analysisName'
    def alertNames = ['firstAlertName', 'secondAlertName']
    def event = new BatchTimedOut(analysisName, alertNames)

    when:
    def result = underTest.toMessageNotifyBatchTimedOut(event)

    then:
    with(result) {
      it.analysisName == analysisName
      alertNamesList == alertNames
    }
  }
}
