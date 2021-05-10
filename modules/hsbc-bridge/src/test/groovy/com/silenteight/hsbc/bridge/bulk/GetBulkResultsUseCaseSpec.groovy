package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade

import spock.lang.Specification

class GetBulkResultsUseCaseSpec extends Specification {

  def fixtures = new Fixtures()
  def alertFacade = Mock(AlertFacade)
  def recommendationFacade = Mock(RecommendationFacade)
  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkResultsUseCase(alertFacade, bulkRepository, recommendationFacade)

  def 'should get bulk results'() {
    given:
    def bulkId = fixtures.bulkId

    when:
    def result = underTest.getResults(bulkId)

    then:
    with(result) {
      bulkId == bulkId
      bulkStatus == com.silenteight.hsbc.bridge.bulk.rest.BulkStatus.COMPLETED
    }

    1 * bulkRepository.findById(bulkId) >> fixtures.bulk
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
