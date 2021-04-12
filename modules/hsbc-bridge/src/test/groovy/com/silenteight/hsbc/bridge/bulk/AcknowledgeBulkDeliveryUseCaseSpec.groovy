package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException

import spock.lang.Specification

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.*

class AcknowledgeBulkDeliveryUseCaseSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new AcknowledgeBulkDeliveryUseCase(bulkRepository)

  def 'should update bulk status to DELIVERY'() {
    given:
    def bulkItem = new BulkItem('100', "".getBytes())
    def bulk = new Bulk('20210101-1111')
    bulk.setStatus(COMPLETED)
    bulk.addItem(bulkItem)
    def updatedBulk = new Bulk('20210101-1111')
    updatedBulk.setStatus(DELIVERED)
    updatedBulk.addItem(bulkItem)

    when:
    def result = underTest.apply(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> bulk
    1 * bulkRepository.save(_ as Bulk) >> updatedBulk
    with(result) {
      bulkId == bulk.id
      bulkStatus.name() == updatedBulk.getStatus().name()
      with(requestedAlerts) {
        size() == bulk.items.size()
        first().id == bulk.items[0].alertExternalId
        first().status.name() == bulk.items[0].status.name()
      }
    }
  }

  def 'should not update bulk status to DELIVERY and throw BulkProcessingNotCompletedException'() {
    given:
    def bulkItem = new BulkItem('100', "".getBytes())
    def bulk = new Bulk('20210101-1111')
    bulk.setStatus(PROCESSING)
    bulk.addItem(bulkItem)

    when:
    def result = underTest.apply(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> bulk
    0 * bulkRepository.save(_ as Bulk)
    def exception = thrown(BulkProcessingNotCompletedException)
    exception.message == "Bulk processing is not completed, id=20210101-1111"
  }
}
