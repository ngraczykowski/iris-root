package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertFacade

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CreateSolvingBulkUseCaseSpec extends Specification {

  private static List<String> ALERTS = List.of('alert/1', 'alert/2', 'alert/3')
  private static String ID_PREFIX = "re-recommend/"
  def bulkRepository = Mock(BulkRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def alertFacade = Mock(AlertFacade)
  def underTest = new CreateSolvingBulkUseCase(bulkRepository, alertFacade, eventPublisher)

  def "Should create bulk and return bulkId"() {
    when:
    def bulkId = underTest.createBulkWithAlerts(ALERTS)

    then:
    bulkId.startsWith(ID_PREFIX)
    1 * bulkRepository.save(_ as Bulk)
    1 * alertFacade.reProcessAlerts(_ as String, ALERTS)
  }
}
