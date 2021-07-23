package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade
import com.silenteight.hsbc.bridge.alert.AlertSender
import com.silenteight.hsbc.bridge.alert.AlertSender.SendOption

import net.javacrumbs.shedlock.core.LockAssert
import spock.lang.Specification

class BulkProcessorSpec extends Specification {

  def adjudicationFacade = Mock(AdjudicationFacade)
  def bulkRepository = Mock(BulkRepository)
  def alertSender = Mock(AlertSender)

  def fixtures = new Fixtures()

  def underTest = new BulkProcessor(adjudicationFacade, alertSender, bulkRepository)

  def 'should process learning bulk'() {
    given:
    LockAssert.TestHelper.makeAllAssertsPass(true)

    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSED) >> Optional.of(fixtures.learningBulk)
    1 * adjudicationFacade.registerAlertWithMatches(_ as Map)
    1 * alertSender.send(_ as Collection, _ as SendOption[])
  }

  def 'should process solving bulk'() {
    given:
    LockAssert.TestHelper.makeAllAssertsPass(true)

    when:
    underTest.processPreProcessedBulks()

    then:
    1 * bulkRepository.findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSED) >> Optional.of(fixtures.solvingBulk)
    1 * adjudicationFacade.registerAlertWithMatchesAndAnalysis(_ as Map) >> 1L
    0 * alertSender.send(_ as Collection, _ as SendOption[])
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
