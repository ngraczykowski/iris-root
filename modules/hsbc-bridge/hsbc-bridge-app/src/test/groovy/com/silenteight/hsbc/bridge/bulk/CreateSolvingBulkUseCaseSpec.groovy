package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.bulk.rest.AlertIdWrapper
import com.silenteight.hsbc.bridge.bulk.rest.AlertReRecommend

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CreateSolvingBulkUseCaseSpec extends Specification {

  private static List<String> ALERTS = List.of('alerts/1', 'alerts/2', 'alerts/3')
  private static String ID_PREFIX = "reRecommend-"
  def bulkRepository = Mock(BulkRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def alertFacade = Mock(AlertFacade)
  def underTest = new CreateSolvingBulkUseCase(bulkRepository, alertFacade, eventPublisher)

  def "Should create bulk and return bulkId"() {
    when:
    def bulkId = underTest.createBulkWithAlerts(alertReRecommend)

    then:
    bulkId.startsWith(ID_PREFIX)
    1 * bulkRepository.save(_ as Bulk)
    1 * alertFacade.reProcessAlerts(_ as String, ALERTS)
  }

  private static alertReRecommend = new AlertReRecommend(
      alerts: [
          new AlertIdWrapper(alertId: 'alerts/1'),
          new AlertIdWrapper(alertId: 'alerts/2'),
          new AlertIdWrapper(alertId: 'alerts/3')
      ]
  )
}
