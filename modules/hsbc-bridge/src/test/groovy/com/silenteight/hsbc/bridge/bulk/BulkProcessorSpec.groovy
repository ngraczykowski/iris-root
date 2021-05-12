package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.match.MatchFacade

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class BulkProcessorSpec extends Specification {

  def alertFacade = Mock(AlertFacade)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def bulkRepository = Mock(BulkRepository)
  def matchFacade = Mock(MatchFacade)

  def fixtures = new Fixtures()

  def underTest = new BulkProcessor(alertFacade, bulkRepository, eventPublisher, matchFacade)

  def 'should process stored bulks and mark error when no alerts has been found'() {
    when:
    underTest.processStoredBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.STORED) >> [fixtures.bulk]
    1 * alertFacade.createAndSaveAlerts(fixtures.bulkId, fixtures.getSomePayload()) >> []
    1 * bulkRepository.save({ Bulk bulk -> bulk.status == BulkStatus.ERROR })
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
