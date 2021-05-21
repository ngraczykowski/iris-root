package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade
import com.silenteight.hsbc.bridge.report.WarehouseFacade

import spock.lang.Specification

class BulkProcessorSpec extends Specification {

  def adjudicationFacade = Mock(AdjudicationFacade)
  def bulkRepository = Mock(BulkRepository)
  def warehouseFacade = Mock(WarehouseFacade)

  def fixtures = new Fixtures()

  def underTest = new BulkProcessor(adjudicationFacade, warehouseFacade, bulkRepository)

  def 'should process learning bulk'() {
    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.PRE_PROCESSED) >> [fixtures.learningBulk]
    1 * adjudicationFacade.registerAlertWithMatches(_ as Map)
    1 * warehouseFacade.findAndSendAlerts(_ as Collection)
  }

  def 'should process solving bulk'() {
    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findByStatus(BulkStatus.PRE_PROCESSED) >> [fixtures.solvingBulk]
    1 * adjudicationFacade.registerAlertWithMatchesAndAnalysis(_ as Map) >> 1L
    0 * warehouseFacade.findAndSendAlerts(_ as Collection)
  }

  class Fixtures {

    String bulkId = 'bulk-1'

    Bulk learningBulk = createBulk(bulkId, true)
    Bulk solvingBulk = createBulk(bulkId, false)

    def createBulk(String bulkId, boolean learning) {
      new Bulk(bulkId, learning)
    }
  }
}
