package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.alert.RawAlert
import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository
import com.silenteight.hsbc.bridge.rest.model.input.Alert
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest

import spock.lang.Specification

class CreateBulkUseCaseSpec extends Specification {

  def alertFacade = Mock(AlertFacade)
  def bulkWriteRepository = Mock(BulkWriteRepository)
  def underTest = new CreateBulkUseCase(alertFacade, bulkWriteRepository)

  def 'should create bulk'() {
    given:
    def request = new HsbcRecommendationRequest(alerts: [new Alert()])

    when:
    def result = underTest.createBulk(request)

    then:
    1 * alertFacade.map(_ as Alert) >> new RawAlert(caseId: 1)
    1 * bulkWriteRepository.save(_ as Bulk) >> { Bulk bulk -> bulk }
    result.bulkId
    result.requestedAlerts.size() == 1
  }
}
