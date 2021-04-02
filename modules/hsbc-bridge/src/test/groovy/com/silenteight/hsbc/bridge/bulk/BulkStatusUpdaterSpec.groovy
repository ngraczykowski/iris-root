package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkStatusEvent
import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.*

class BulkStatusUpdaterSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new BulkStatusUpdater(bulkRepository)

  @Unroll
  def 'should update BulkStatus'() {
    given:
    def bulk = createBulkwithBulkItemCombination(itemStatuses)
    def event = new UpdateBulkStatusEvent(bulk.getId())

    when:
    underTest.onBulkStatusEvent(event)

    then:
    1 * bulkRepository.findById(bulk.getId()) >> bulk
    1 * bulkRepository.save({Bulk b -> b.status == expectedResult})

    where:
    itemStatuses             | expectedResult
    [PROCESSING, ERROR]      | PROCESSING
    [PROCESSING, COMPLETED]  | PROCESSING
    [PROCESSING, PROCESSING] | PROCESSING
    [COMPLETED, COMPLETED]   | COMPLETED
    [COMPLETED, ERROR]       | COMPLETED
    [ERROR, ERROR]           | COMPLETED
  }

  @Unroll
  def 'should not update BulkStatus'() {
    given:
    def bulk = createBulkwithBulkItemCombination(itemStatuses)
    def event = new UpdateBulkStatusEvent(bulk.getId())

    when:
    underTest.onBulkStatusEvent(event)

    then:
    1 * bulkRepository.findById(bulk.getId()) >> bulk
    0 * bulkRepository.save({Bulk b -> b.status == expectedResult})

    where:
    itemStatuses | expectedResult
    []           | STORED
  }

  def createBulkwithBulkItemCombination(itemStatuses) {
    def bulk = new Bulk()

    if (itemStatuses.isEmpty()) {
      bulk.setStatus(STORED)
      bulk
    } else {
      def item_1 = new BulkItem(1, ''.getBytes())
      item_1.setBulkId(bulk.getId())
      item_1.setStatus(itemStatuses.first())

      def item_2 = new BulkItem(1, ''.getBytes())
      item_2.setBulkId(bulk.getId())
      item_2.setStatus(itemStatuses.get(1))

      bulk.setStatus(STORED)
      bulk.addItem(item_1)
      bulk.addItem(item_2)
      bulk
    }
  }
}
