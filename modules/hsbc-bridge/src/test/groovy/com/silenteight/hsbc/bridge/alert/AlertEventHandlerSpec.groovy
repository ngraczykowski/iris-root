package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent
import com.silenteight.hsbc.bridge.bulk.BulkStatus
import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkItemStatusEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static java.util.Optional.of

class AlertEventHandlerSpec extends Specification {

  def alertRepository = Mock(AlertRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def underTest = new AlertEventHandler(alertRepository, eventPublisher)

  def 'should handle alert update name event'() {
    given:
    def someAlertEntity = new AlertEntity(1, 2, "".getBytes())
    def event = new UpdateAlertWithNameEvent([1L: "alert/1"] as Map)

    when:
    underTest.onUpdateAlertEventWithNameEvent(event)

    then:
    1 * alertRepository.findById(1) >> of(someAlertEntity)
    1 * alertRepository.save({AlertEntity a -> a.name == "alert/1"})
    1 * eventPublisher.
        publishEvent(
            {UpdateBulkItemStatusEvent e -> e.bulkItemId == 2 && e.newStatus == BulkStatus.PROCESSING})
  }

  def 'should handle alert recommendation ready event'() {
    given:
    def alertName = "alert/1"
    def someAlertEntity = new AlertEntity(1, 2, "".getBytes())
    def event = new AlertRecommendationReadyEvent(alertName)

    when:
    underTest.onAlertRecommendationReadyEvent(event)

    then:
    1 * alertRepository.findByName(alertName) >> of(someAlertEntity)
    1 * eventPublisher.
        publishEvent(
            {UpdateBulkItemStatusEvent e -> e.bulkItemId == 2 && e.newStatus == BulkStatus.COMPLETED})
  }
}
