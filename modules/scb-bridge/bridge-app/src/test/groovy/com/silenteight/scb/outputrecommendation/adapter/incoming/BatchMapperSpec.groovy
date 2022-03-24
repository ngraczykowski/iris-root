package com.silenteight.scb.outputrecommendation.adapter.incoming

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.proto.registration.api.v1.MessageBatchError

import spock.lang.Specification
import spock.lang.Subject

class BatchMapperSpec extends Specification {

  @Subject
  def underTest = new BatchMapper()

  def "should map MessageBatchCompleted to command"() {
    given:
    def message = MessageBatchCompleted.newBuilder()
        .setBatchId(Fixtures.BATCH_ID)
        .setAnalysisName(Fixtures.ANALYSIS_ID)
        .setBatchMetadata(Fixtures.BATCH_METADATA)
        .build()

    when:
    def command = underTest.fromBatchCompletedMessage(message)

    then:
    with(command) {
      batchId() == Fixtures.BATCH_ID
      analysisName() == Fixtures.ANALYSIS_ID
      alertNames() == []
      batchMetadata() == Fixtures.BATCH_METADATA
    }
  }

  def "should map MessageBatchError to command"() {
    given:
    def message = MessageBatchError.newBuilder()
        .setBatchId(Fixtures.BATCH_ID)
        .setBatchMetadata(Fixtures.BATCH_METADATA)
        .setErrorDescription(Fixtures.ERROR_DESCRIPTION)
        .build()

    when:
    def command = underTest.fromBatchErrorMessage(message)

    then:
    with(command) {
      batchId() == Fixtures.BATCH_ID
      errorDescription() == Fixtures.ERROR_DESCRIPTION
      batchMetadata() == Fixtures.BATCH_METADATA
    }
  }
}
