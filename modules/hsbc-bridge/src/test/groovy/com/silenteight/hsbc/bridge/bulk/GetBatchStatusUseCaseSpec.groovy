package com.silenteight.hsbc.bridge.bulk

import spock.lang.Specification

class GetBatchStatusUseCaseSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkStatusUseCase(bulkRepository)

  def 'should get batch status'() {
    given:
    def bulk = new Bulk('20210101-1111')

    when:
    def result = underTest.getStatus(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> Optional.of(bulk)

    with(result) {
      batchId == bulk.id
      batchStatus.name() == bulk.status.name()
      with(requestedAlerts) {
      }
    }
  }
}
