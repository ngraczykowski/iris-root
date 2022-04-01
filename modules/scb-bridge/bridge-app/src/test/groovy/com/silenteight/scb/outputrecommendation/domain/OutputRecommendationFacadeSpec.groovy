package com.silenteight.scb.outputrecommendation.domain

import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.scb.ingest.domain.model.BatchStatus.COMPLETED
import static com.silenteight.scb.ingest.domain.model.BatchStatus.ERROR

class OutputRecommendationFacadeSpec extends Specification {

  def recommendationsProcessor = Mock(RecommendationsProcessor)
  def batchInfoService = Mock(BatchInfoService)

  @Subject
  def underTest = new OutputRecommendationFacade(recommendationsProcessor, batchInfoService)

  def 'should invoke process batch completed'() {
    given:
    def command = PrepareRecommendationResponseCommand.builder().build()

    when:
    underTest.prepareCompletedBatchRecommendations(command)

    then:
    1 * recommendationsProcessor.processBatchCompleted(command)
    1 * batchInfoService.changeStatus(command.batchId(), COMPLETED)
  }

  def 'should invoke process batch error'() {
    given:
    def command = PrepareErrorRecommendationResponseCommand.builder().build()

    when:
    underTest.prepareErrorBatchRecommendations(command)

    then:
    1 * recommendationsProcessor.processBatchError(command)
    1 * batchInfoService.changeStatus(command.batchId(), ERROR)
  }
}
