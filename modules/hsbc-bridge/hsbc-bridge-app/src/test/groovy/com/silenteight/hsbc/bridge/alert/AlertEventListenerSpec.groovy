package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent
import com.silenteight.hsbc.bridge.analysis.event.RecalculateAnalysisStatusEvent
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationInfo
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent

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
    def analysis = 'analysis'
    def alert = 'alert/1'
    def recommendation = 'recommendation/1'
    def alertRecommendationInfo = new AlertRecommendationInfo(alert, recommendation)
    def recommendationInfos = [alertRecommendationInfo]
    def event = new RecommendationsGeneratedEvent(analysis, recommendationInfos)

    when:
    underTest.onAlertRecommendationReadyEvent(event)

    then:
    1 * updater.updateWithRecommendationReadyStatus([alert])
    1 * eventPublisher.publishEvent(_ as RecalculateAnalysisStatusEvent)
  }
}
