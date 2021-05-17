package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade
import com.silenteight.hsbc.bridge.report.WarehouseClient

import spock.lang.Specification

class BulkProcessorSpec extends Specification {

  def adjudicationFacade = Mock(AdjudicationFacade)
  def bulkRepository = Mock(BulkRepository)
  def warehouseClient = Mock(WarehouseClient)

  def fixtures = new Fixtures()

  def underTest = new BulkProcessor(adjudicationFacade, bulkRepository, warehouseClient)

  def 'should process learning bulk'() {
    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.PRE_PROCESSED) >> [fixtures.learningBulk]
    1 * adjudicationFacade.registerAlertWithMatches(_ as Map)
    1 * warehouseClient.sendAlerts(_ as Collection)
  }

  def 'should process solving bulk'() {
    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.PRE_PROCESSED) >> [fixtures.solvingBulk]
    1 * adjudicationFacade.registerAlertWithMatchesAndAnalysis(_ as Map) >> 1L
    0 * warehouseClient.sendAlerts(_ as Collection)
  }

  class Fixtures {

    String bulkId = 'bulk-1'
    byte[] somePayload = 'somePayload'.getBytes()
    BulkPayloadEntity bulkPayload = new BulkPayloadEntity(somePayload)

    Bulk learningBulk = createBulk(bulkId, bulkPayload, true)
    Bulk solvingBulk = createBulk(bulkId, bulkPayload, false)

    def createBulk(String bulkId, bulkPayload, boolean learning) {
      var bulk = new Bulk(bulkId, learning)
      bulk.setPayload(bulkPayload)
      bulk
    }
  }
}
