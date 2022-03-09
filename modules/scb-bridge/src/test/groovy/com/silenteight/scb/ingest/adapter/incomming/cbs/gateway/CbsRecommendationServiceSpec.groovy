package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails
import com.silenteight.proto.serp.v1.alert.Alert
import com.silenteight.proto.serp.v1.common.ObjectId
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation
import com.silenteight.proto.serp.v1.recommendation.Recommendation
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction

import com.google.protobuf.Any
import spock.lang.Specification

class CbsRecommendationServiceSpec extends Specification {

  def cbsRecommendationGateway = Mock(CbsRecommendationGateway)
  def recommendationValues = ['ACTION_FALSE_POSITIVE': 'False Positive']

  def 'should recommend alert only for watchlist level processing'() {
    when:
    new CbsRecommendationService(cbsRecommendationGateway, [:])

    then:
    0 * _
  }

  def 'should send watchlist-level alert recommendation to cbs and use user-friendly status'() {
    given:
    def alertRecommendation = AlertRecommendation.newBuilder()
        .setAlert(
            Alert.newBuilder()
                .setDetails(Any.pack(createAlertDetailsWatchlistLevel()))
                .build())
        .setRecommendation(createRecommendation())
        .build()

    when:
    new CbsRecommendationService(cbsRecommendationGateway, recommendationValues)
        .recommend([alertRecommendation])

    then:
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.size() == 1 && verifyCbsAlert(it.first())
        })
  }

  def verifyCbsAlert(CbsAlertRecommendation cbsAlert) {
    cbsAlert.hitRecommendedStatus == 'False Positive'
    cbsAlert.hitRecommendedComments == 'comment'
    cbsAlert.alertExternalId == 'system-id'
    cbsAlert.hitWatchlistId == 'watchlist-id'
    cbsAlert.batchId == 'batch-id'
  }

  def 'should do not send alert-level alert recommendation to cbs'() {
    given:
    def alertRecommendation = AlertRecommendation.newBuilder()
        .setAlert(
            Alert.newBuilder()
                .setDetails(Any.pack(createAlertDetailsAlertLevel()))
                .build())
        .setRecommendation(createRecommendation())
        .build()

    when:
    new CbsRecommendationService(cbsRecommendationGateway, [:])
        .recommend([alertRecommendation])

    then:
    1 * cbsRecommendationGateway.recommendAlerts(
        {
          it.isEmpty()
        })
  }

  def 'should not recommend alert when scbAlertDetails are not present'() {
    given:
    def alert = Alert.newBuilder()
        .setId(ObjectId.newBuilder().setSourceId('EN_BTCH_ALLOW!fe660195').build())
        .build()
    def alertRecommendation = AlertRecommendation.newBuilder()
        .setAlert(alert)
        .setRecommendation(createRecommendation())
        .build()

    when:
    new CbsRecommendationService(cbsRecommendationGateway, recommendationValues)
        .recommend([alertRecommendation])

    then:
    0 * _
    def ex = thrown(IllegalArgumentException)
    ex.message == 'Alert has no details'
  }

  def createAlertDetailsWatchlistLevel() {
    ScbAlertDetails.newBuilder()
        .setBatchId('batch-id')
        .setSystemId('system-id')
        .setWatchlistId('watchlist-id')
        .build()
  }

  def createAlertDetailsAlertLevel() {
    ScbAlertDetails.newBuilder()
        .setBatchId('batch-id')
        .setSystemId('system-id')
        .build()
  }

  def createRecommendation() {
    Recommendation.newBuilder()
        .setComment('comment')
        .setAction(RecommendedAction.ACTION_FALSE_POSITIVE)
        .build()
  }
}
