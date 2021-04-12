package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.bulk.rest.input.SolvedAlertStatus
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto
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
      bulkStatus == com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus.COMPLETED
      alerts.size() == 1
      with(alerts.first()) {
        id == fixtures.bulkItem.alertExternalId
        recommendation == SolvedAlertStatus.FALSE_POSITIVE
        comment == fixtures.recommendationComment
      }
    }

    1 * bulkRepository.findById(bulkId) >> fixtures.bulk
    1 * alertFacade.getAlertNameByBulkId(fixtures.bulkItemId) >> fixtures.alert
    1 * recommendationFacade.getRecommendation(fixtures.alert) >> fixtures.recommendation
  }

  class Fixtures {

    def bulkItem = new BulkItem(id: 1, alertExternalId: 200)
    def bulkItemId = bulkItem.id
    def bulk = createBulk()
    def bulkId = bulk.id;
    def alert = 'alert/1'
    def recommendationComment = 'Comment'
    def recommendedAction = 'FALSE_POSITIVE'
    def recommendation = RecommendationDto.builder()
        .alert(alert)
        .recommendationComment(recommendationComment)
        .recommendedAction(recommendedAction)
        .name('recommendation/alert/1')
        .build()

    def createBulk() {
      def bulk = new Bulk('20210101-1111')
      bulk.setStatus(BulkStatus.COMPLETED)
      bulk.addItem(bulkItem)
      bulk
    }
  }
}
