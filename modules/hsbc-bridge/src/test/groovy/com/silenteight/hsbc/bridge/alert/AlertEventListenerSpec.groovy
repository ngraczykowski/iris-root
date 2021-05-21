package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class AlertEventListenerSpec extends Specification {

  def alertProcessor = Mock(AlertProcessor)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def updater = Mock(AlertUpdater)
  def underTest = new AlertEventListener(alertProcessor, updater, eventPublisher)

  def 'should handle alert update name event'() {
    given:
    def alertIdToName = [1L: "alert/1"]
    def event = new UpdateAlertWithNameEvent(alertIdToName)

    when:
    underTest.onUpdateAlertEventWithNameEvent(event)

    then:
    1 * updater.updateNames(alertIdToName)
  }

  def 'should handle alert recommendation ready event'() {
    given:
    def alertName = "alert/1"
    def event = new AlertRecommendationReadyEvent(alertName)

    when:
    underTest.onAlertRecommendationReadyEvent(event)

    then:
    1 * updater.updateWithCompletedStatus(alertName)
  }
}
