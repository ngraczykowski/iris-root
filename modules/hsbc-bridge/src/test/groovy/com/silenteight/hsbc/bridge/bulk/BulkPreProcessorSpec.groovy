package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.match.MatchFacade

import spock.lang.Specification

class BulkPreProcessorSpec extends Specification {

  def alertFacade = Mock(AlertFacade)
  def bulkRepository = Mock(BulkRepository)
  def matchFacade = Mock(MatchFacade)

  def fixtures = new Fixtures()

  def underTest = new BulkPreProcessor(alertFacade, bulkRepository, matchFacade)

  def 'should process stored bulks and mark error when no alerts has been found'() {
    when:
    underTest.processStoredBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.STORED) >> [fixtures.bulk]
    1 * alertFacade.createAndSaveAlerts(fixtures.bulkId, fixtures.getSomePayload()) >> []
  }

  class Fixtures {

    String bulkId = 'bulk-1'
    byte[] somePayload = 'somePayload'.getBytes()
    BulkPayloadEntity bulkPayload = new BulkPayloadEntity(somePayload)

    Bulk bulk = createBulk(bulkId, bulkPayload)

    def createBulk(bulkId, bulkPayload) {
      var bulk = new Bulk(bulkId)
      bulk.setPayload(bulkPayload)
      bulk
    }
  }
}
