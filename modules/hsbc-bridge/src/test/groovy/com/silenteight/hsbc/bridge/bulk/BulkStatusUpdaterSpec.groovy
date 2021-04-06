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
  def 'should update BulkStatus to #expectedResult when #itemStatuses'() {
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
    [PROCESSING]             | PROCESSING
    [PROCESSING, ERROR]      | PROCESSING
    [PROCESSING, COMPLETED]  | PROCESSING
    [PROCESSING, PROCESSING] | PROCESSING
    [ERROR]                  | COMPLETED
    [COMPLETED]              | COMPLETED
    [COMPLETED, COMPLETED]   | COMPLETED
    [COMPLETED, ERROR]       | COMPLETED
    [ERROR, ERROR]           | COMPLETED
  }

  @Unroll
  def 'should not update BulkStatus when itemStatuses = #itemStatuses'() {
    given:
    def bulk = createBulkwithBulkItemCombination(itemStatuses)
    def event = new UpdateBulkStatusEvent(bulk.getId())

    when:
    underTest.onBulkStatusEvent(event)

    then:
    1 * bulkRepository.findById(bulk.getId()) >> bulk
    0 * bulkRepository.save(_ as Bulk)

    where:
    itemStatuses << [
        [],
        [STORED],
        [CANCELLED],
        [STORED, STORED],
        [STORED, CANCELLED],
        [CANCELLED, CANCELLED]
    ]
  }

  def createBulkwithBulkItemCombination(itemStatuses) {
    def bulk = new Bulk('20210101-1111')

    if (itemStatuses.isEmpty()) {
      bulk.setStatus(STORED)
      bulk
    } else {
      itemStatuses.forEach(
          status -> {
            def item = new BulkItem(1, ''.getBytes())
            item.setBulkId(bulk.getId())
            item.setStatus(status)
            bulk.setStatus(STORED)
            bulk.addItem(item)
          }
      )
      bulk
    }
  }
}
