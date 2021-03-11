package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.RawAlert
import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository

import spock.lang.Specification

class GetBulkStatusUseCaseSpec extends Specification {

  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkStatusUseCase(bulkRepository)

  def 'should get bulk status'() {
    given:
    var alert = new RawAlert(caseId: 100)
    var bulkItem = new BulkItem(alert)
    def bulk = new Bulk(items: [bulkItem])

    when:
    def result = underTest.getStatus(bulk.id)

    then:
    1 * bulkRepository.findById(_ as UUID) >> bulk
    with(result) {
      bulkId == bulk.id
      bulkStatus.name() == bulk.status.name()
      with(requestedAlerts) {
        size() == bulk.items.size()
        first().id == bulk.items[0].alert.caseId
        first().status.name() == bulk.items[0].status.name()
      }
    }
  }
}
