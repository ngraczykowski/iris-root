package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.AlertStatus
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import java.time.OffsetDateTime

class GnsRtFixtures {

  def alertId = ObjectId.builder()
      .sourceId("alertId")
      .discriminator("discriminator")
      .build()

  def alert = alert(alertId)

  def alerts = [alert]

  def gnsAlert = gnsAlert(alertId)

  def gnsRtRecommendationRequest = gnsRtRecommendationRequest(gnsAlert)

  static gnsAlert(ObjectId id) {
    def alert = new GnsRtAlert()
    alert.setAlertId(id.sourceId())
    alert.setWatchlistType("AM")
    alert
  }

  static alert(ObjectId id) {
    Alert.builder()
        .id(id)
        .build()
  }
  
  def recommendation = Recommendation.builder()
      .alert(
          Recommendations.Alert.builder()
              .id("alertId")
              .status(AlertStatus.SUCCESS)
              .metadata("")
              .errorMessage("")
              .build())
      .matches([])
      .batchId("batchId")
      .name("name")
      .recommendedAction(RecommendedAction.ACTION_INVESTIGATE)
      .recommendedComment("comment")
      .policyId("policyId")
      .recommendedAt(OffsetDateTime.now())
      .build()

  def recommendations = Recommendations.builder()
      .recommendations([recommendation])
      .build()

  private static gnsRtRecommendationRequest(GnsRtAlert... alerts) {
    def immediateResponseData = new ImmediateResponseData()
    immediateResponseData.setAlerts(alerts as List)

    def screenCustomerNameResInfo = new GnsRtScreenCustomerNameResInfo()
    screenCustomerNameResInfo.setImmediateResponseData(immediateResponseData)

    def screenCustomerNameResPayload = new GnsRtScreenCustomerNameResPayload()
    screenCustomerNameResPayload.setScreenCustomerNameResInfo(screenCustomerNameResInfo)

    def originationDetails = new GnsRtOriginationDetails()
    originationDetails.setTrackingId(UUID.randomUUID().toString())

    def screenCustomerNameResHeader = new GnsRtScreenCustomerNameResHeader()
    screenCustomerNameResHeader.setOriginationDetails(originationDetails)

    def screenCustomerNameRes = new GnsRtScreenCustomerNameRes()
    screenCustomerNameRes.setScreenCustomerNameResPayload(screenCustomerNameResPayload)
    screenCustomerNameRes.setHeader(screenCustomerNameResHeader)

    def recommendationRequest = new GnsRtRecommendationRequest()
    recommendationRequest.setScreenCustomerNameRes(screenCustomerNameRes)

    recommendationRequest
  }

}
