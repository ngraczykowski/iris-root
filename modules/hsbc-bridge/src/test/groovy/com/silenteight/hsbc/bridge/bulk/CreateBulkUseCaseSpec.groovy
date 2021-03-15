package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent
import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository
import com.silenteight.hsbc.bridge.rest.model.input.Alert
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation
import com.silenteight.hsbc.bridge.rest.model.input.CasesWithAlertURL
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CreateBulkUseCaseSpec extends Specification {

  def alertFacade = Mock(AlertFacade)
  def bulkWriteRepository = Mock(BulkWriteRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def underTest = new CreateBulkUseCase(alertFacade, bulkWriteRepository, eventPublisher)

  def 'should create bulk'() {
    given:
    def alert = new Alert(
        systemInformation: new AlertSystemInformation(
            casesWithAlertURL: [new CasesWithAlertURL(id: 100)]
        ))
    def request = new HsbcRecommendationRequest(alerts: [alert])

    when:
    def result = underTest.createBulk(request)

    then:
    1 * alertFacade.convertToPayload(_ as Alert) >> "".getBytes()
    1 * bulkWriteRepository.save(_ as Bulk) >> {Bulk bulk -> bulk}
    1 * eventPublisher.publishEvent(_ as BulkStoredEvent)
    result.bulkId
    result.requestedAlerts.size() == 1
  }
}
