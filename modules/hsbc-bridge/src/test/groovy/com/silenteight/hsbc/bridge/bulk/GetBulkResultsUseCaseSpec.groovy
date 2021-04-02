package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade
import com.silenteight.hsbc.bridge.bulk.rest.input.SolvedAlertStatus

import spock.lang.Specification

class GetBulkResultsUseCaseSpec extends Specification {

  def fixtures = new Fixtures()
  def alertFacade = Mock(AlertFacade)
  def recommendationFacade = Mock(RecommendationFacade)
  def bulkRepository = Mock(BulkQueryRepository)
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

    BulkItem bulkItem = new BulkItem(id: 1, alertExternalId: 200)
    long bulkItemId = bulkItem.id
    Bulk bulk = new Bulk(status: BulkStatus.COMPLETED, items: [bulkItem])
    UUID bulkId = bulk.id;

    String alert = 'alert/1'
    String recommendationComment = 'Comment'
    String recommendedAction = 'FALSE_POSITIVE'

    RecommendationDto recommendation = RecommendationDto.builder()
        .alert(alert)
        .recommendationComment(recommendationComment)
        .recommendedAction(recommendedAction)
        .name('recommendation/alert/1')
        .build()
  }
}
