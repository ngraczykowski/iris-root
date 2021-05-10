package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent

import spock.lang.Specification

class AlertEventListenerSpec extends Specification {

  def updater = Mock(AlertUpdater)
  def underTest = new AlertEventListener(updater)

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
