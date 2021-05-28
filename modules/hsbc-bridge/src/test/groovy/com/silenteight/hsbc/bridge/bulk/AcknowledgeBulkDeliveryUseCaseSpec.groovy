package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException

import spock.lang.Specification

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.*

class AcknowledgeBulkDeliveryUseCaseSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new AcknowledgeBulkDeliveryUseCase(bulkRepository)

  def 'should update bulk status to DELIVERY'() {
    given:
    def bulk = new Bulk('20210101-1111')
    bulk.setStatus(COMPLETED)
    def updatedBulk = new Bulk('20210101-1111')
    updatedBulk.setStatus(DELIVERED)

    when:
    def result = underTest.apply(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> Optional.of(bulk)
    1 * bulkRepository.save(_ as Bulk) >> updatedBulk
    with(result) {
      batchId == bulk.id
      batchStatus.name() == updatedBulk.getStatus().name()
      with(requestedAlerts) {

      }
    }
  }

  def 'should not update bulk status to DELIVERY and throw BulkProcessingNotCompletedException'() {
    given:
    def bulk = new Bulk('20210101-1111')
    bulk.setStatus(PROCESSING)

    when:
    underTest.apply(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> Optional.of(bulk)
    0 * bulkRepository.save(_ as Bulk)
    def exception = thrown(BatchProcessingNotCompletedException)
    exception.message == "Batch processing is not completed, id=20210101-1111"
  }
}
