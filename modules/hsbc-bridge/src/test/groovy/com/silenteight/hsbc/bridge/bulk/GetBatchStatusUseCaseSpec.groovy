package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.analysis.AnalysisFacade

import spock.lang.Specification

class GetBatchStatusUseCaseSpec extends Specification {

  def analysisFacade = Mock(AnalysisFacade)
  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkStatusUseCase(analysisFacade, bulkRepository)

  def 'should get batch status'() {
    given:
    def bulk = new Bulk('20210101-1111')

    when:
    def result = underTest.getStatus(bulk.id)

    then:
    1 * bulkRepository.findById(_ as String) >> Optional.of(bulk)
    0 * analysisFacade.getById(_)

    with(result) {
      batchId == bulk.id
      batchStatus.name() == bulk.status.name()
      with(requestedAlerts) {
      }
    }
  }
}
