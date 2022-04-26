package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import spock.lang.Specification

class CbsRecommendationServiceSpec extends Specification {

  def cbsRecommendationGateway = Mock(CbsRecommendationGateway)
  def cbsRecommendationMapper = Mock(CbsRecommendationMapper)
  def alertInFlightService = Mock(AlertInFlightService)
  def recommendedStatus = 'False Positive'
  def payload = 'payload'
  def alertId = 'alertId'
  def batchId = 'batchId'
  def comment = 'comment'
  def watchlistId = 'watchlistId'

  def 'should recommend alert only for watchlist level processing'() {
    when:
    new CbsRecommendationService(
        cbsRecommendationGateway, cbsRecommendationMapper, alertInFlightService)

    then:
    0 * _
  }

  def 'should send watchlist-level alert recommendation to cbs and use user-friendly status'() {
    given:
    def alertRecommendation = createRecommendation()
    def cbsAlertRecommendation = createCbsAlertRecommendation()

    when:
    new CbsRecommendationService(
        cbsRecommendationGateway, cbsRecommendationMapper, alertInFlightService)
        .recommend([alertRecommendation])

    then:
    1 * cbsRecommendationMapper.getAlertsToBeRecommended([alertRecommendation]) >>
        [cbsAlertRecommendation]
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.size() == 1 && verifyCbsAlert(it.first())
        })
    1 * alertInFlightService.deleteAlerts(_ as List<AlertId>)
  }

  def verifyCbsAlert(CbsAlertRecommendation cbsAlert) {
    cbsAlert.hitRecommendedStatus == recommendedStatus
    cbsAlert.hitRecommendedComments == comment
    cbsAlert.alertExternalId == alertId
    cbsAlert.hitWatchlistId == watchlistId
    cbsAlert.batchId == batchId
  }

  def 'should do not send alert-level alert recommendation to cbs'() {
    given:
    def alertRecommendation = createRecommendation()

    when:
    new CbsRecommendationService(
        cbsRecommendationGateway, cbsRecommendationMapper, alertInFlightService)
        .recommend([alertRecommendation])

    then:
    1 * cbsRecommendationMapper.getAlertsToBeRecommended([alertRecommendation]) >> []
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.isEmpty()
        })
    1 * alertInFlightService.deleteAlerts(_ as List<AlertId>)
  }

  def createAlert() {
    Alert.builder()
        .id(alertId)
        .metadata(payload)
        .build()
  }

  def createRecommendation() {
    Recommendation.builder()
        .alert(createAlert())
        .batchId(batchId)
        .recommendedComment(comment)
        .recommendedAction(RecommendedAction.ACTION_FALSE_POSITIVE)
        .build()
  }

  def createCbsAlertRecommendation() {
    CbsAlertRecommendation.builder()
        .alertExternalId(alertId)
        .batchId(batchId)
        .hitRecommendedComments(comment)
        .hitRecommendedStatus(recommendedStatus)
        .build()
  }
}
