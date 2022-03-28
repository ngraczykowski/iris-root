package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.scb.ingest.domain.model.AlertMetadata
import com.silenteight.scb.ingest.domain.payload.PayloadConverter
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import spock.lang.Specification

class CbsRecommendationServiceSpec extends Specification {

  def cbsRecommendationGateway = Mock(CbsRecommendationGateway)
  def payloadConverter = Mock(PayloadConverter)
  def recommendationValues = ['ACTION_FALSE_POSITIVE': 'False Positive']
  def payload = 'payload'
  def alertId = 'alertId'
  def batchId = 'batchId'
  def comment = 'comment'
  def watchlistId = 'watchlistId'

  def 'should recommend alert only for watchlist level processing'() {
    when:
    new CbsRecommendationService(cbsRecommendationGateway, [:], payloadConverter)

    then:
    0 * _
  }

  def 'should send watchlist-level alert recommendation to cbs and use user-friendly status'() {
    given:
    def alertRecommendation = createRecommendation()
    def alertMetadata = new AlertMetadata(watchlistId, 'discriminator')

    when:
    new CbsRecommendationService(cbsRecommendationGateway, recommendationValues, payloadConverter)
        .recommend([alertRecommendation])

    then:
    2 * payloadConverter.deserializeFromJsonToObject(payload, AlertMetadata.class) >> alertMetadata
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.size() == 1 && verifyCbsAlert(it.first())
        })
  }

  def verifyCbsAlert(CbsAlertRecommendation cbsAlert) {
    cbsAlert.hitRecommendedStatus == 'False Positive'
    cbsAlert.hitRecommendedComments == comment
    cbsAlert.alertExternalId == alertId
    cbsAlert.hitWatchlistId == watchlistId
    cbsAlert.batchId == batchId
  }

  def 'should do not send alert-level alert recommendation to cbs'() {
    given:
    def alertRecommendation = createRecommendation()
    def alertMetadata = new AlertMetadata(null, 'discriminator')

    when:
    new CbsRecommendationService(cbsRecommendationGateway, [:], payloadConverter)
        .recommend([alertRecommendation])

    then:
    1 * payloadConverter.deserializeFromJsonToObject(payload, AlertMetadata.class) >> alertMetadata
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.isEmpty()
        })
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
}
