package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase

import spock.lang.Specification

class GetBulkResultsUseCaseSpec extends Specification {

  def fixtures = new Fixtures()
  def recommendationFacade = Mock(GetRecommendationUseCase)
  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkResultsUseCase(bulkRepository, recommendationFacade)

  def 'should get bulk results'() {
    given:
    def bulkId = fixtures.bulkId

    when:
    def result = underTest.getResults(bulkId)

    then:
    with(result) {
      bulkId == bulkId
      batchStatus == BatchStatus.COMPLETED
    }

    1 * bulkRepository.findById(bulkId) >> Optional.of(fixtures.bulk)
  }

  class Fixtures {

    def bulk = createBulk()
    def bulkId = bulk.id;

    def createBulk() {
      def bulk = new Bulk('20210101-1111')
      bulk.setStatus(BulkStatus.COMPLETED)
      bulk
    }
  }
}
