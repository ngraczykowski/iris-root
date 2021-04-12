package com.silenteight.hsbc.bridge.bulk

import spock.lang.Specification

class GetBulkStatusUseCaseSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkStatusUseCase(bulkRepository)

  def 'should get bulk status'() {
    given:
    def bulkItem = new BulkItem('100', "".getBytes())
    def bulk = new Bulk('20210101-1111')
    bulk.addItem(bulkItem)

    when:
    def result = underTest.getStatus(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> bulk
    with(result) {
      bulkId == bulk.id
      bulkStatus.name() == bulk.status.name()
      with(requestedAlerts) {
        size() == bulk.items.size()
        first().id == bulk.items[0].alertExternalId
        first().status.name() == bulk.items[0].status.name()
      }
    }
  }
}
