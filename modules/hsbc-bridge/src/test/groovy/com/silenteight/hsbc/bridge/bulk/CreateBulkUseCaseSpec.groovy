package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent
import com.silenteight.hsbc.bridge.bulk.rest.input.Alert
import com.silenteight.hsbc.bridge.bulk.rest.input.AlertSystemInformation
import com.silenteight.hsbc.bridge.bulk.rest.input.CasesWithAlertURL
import com.silenteight.hsbc.bridge.bulk.rest.input.HsbcRecommendationRequest

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED

class CreateBulkUseCaseSpec extends Specification {

  def eventPublisher = Mock(ApplicationEventPublisher)
  def bulkProvider = Mock(BulkProvider)
  def underTest = new CreateBulkUseCase(bulkProvider, eventPublisher)

  def 'should create bulk'() {
    given:
    def alert = new Alert(
        systemInformation: new AlertSystemInformation(
            casesWithAlertURL: [new CasesWithAlertURL(id: 100)]
        ))
    def request = new HsbcRecommendationRequest(alerts: [alert])
    def bulk = createBulk()

    when:
    bulkProvider.getBulk(request) >> bulk
    def result = underTest.createBulk(request)

    then:
    1 * eventPublisher.publishEvent(_ as BulkStoredEvent)
    result.bulkId
    result.requestedAlerts.size() == 1
  }

  def createBulk() {
    def bulkItem = new BulkItem(100, "".getBytes())
    def bulk = new Bulk('20210101-1111')
    bulk.setStatus(COMPLETED)
    bulk.addItem(bulkItem)
    bulk
  }
}
