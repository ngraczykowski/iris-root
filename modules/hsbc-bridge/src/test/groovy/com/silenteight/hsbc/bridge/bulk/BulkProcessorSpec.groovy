package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade
import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.alert.LearningAlertProcessor

import net.javacrumbs.shedlock.core.LockAssert
import spock.lang.Specification

import java.util.stream.Stream

class BulkProcessorSpec extends Specification {

  def adjudicationFacade = Mock(AdjudicationFacade)
  def alertFacade = Mock(AlertFacade)
  def bulkRepository = Mock(BulkRepository)
  def learningAlertProcessor = Mock(LearningAlertProcessor)

  def fixtures = new Fixtures()

  def underTest = new BulkProcessor(
      adjudicationFacade,
      alertFacade,
      learningAlertProcessor,
      bulkRepository)

  def 'should process learning bulk'() {
    given:
    LockAssert.TestHelper.makeAllAssertsPass(true)

    when:
    underTest.tryToProcessLearningBulk(fixtures.learningBulk.id)

    then:
    1 * bulkRepository.findById(fixtures.learningBulk.id) >>
        Optional.of(fixtures.learningBulk)
    1 * alertFacade.getRegisteredAlertsFromDb(_ as Stream<String>) >> []
    0 * adjudicationFacade.registerAlertWithMatches(_ as Map)
    1 * learningAlertProcessor.process(_ as Collection, _ as Collection, _ as Collection)
  }

  def 'should process solving bulk'() {
    given:
    LockAssert.TestHelper.makeAllAssertsPass(true)

    when:
    underTest.tryToProcessSolvingBulk(fixtures.solvingBulk.id)

    then:
    1 * bulkRepository.findById(fixtures.solvingBulk.id) >>
        Optional.of(fixtures.solvingBulk)
    1 * adjudicationFacade.registerAlertWithMatchesAndAnalysis(_ as Map) >> 1L
    0 * learningAlertProcessor.process(_ as Collection, _ as Collection)
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
