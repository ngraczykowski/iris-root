/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtScreenCustomerNameRes
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtScreenCustomerNameResInfo
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtScreenCustomerNameResPayload
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.ImmediateResponseData
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.AlertStatus
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import java.time.OffsetDateTime

class GnsRtFixtures {

  def alertId = ObjectId.builder()
      .sourceId("alertId")
      .discriminator("discriminator")
      .build()

  def alert = alert(alertId)

  def alerts = [alert]

  def gnsAlert = gnsAlert(alertId)

  def gnsRtRecommendationRequest = gnsRtRecommendationRequest([gnsAlert])

  static gnsAlert(ObjectId id) {
    new GnsRtAlert(
        alertId: id.sourceId(),
        watchlistType: "AM"
    )
  }

  static alert(ObjectId id) {
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
        .id(id)
        .details(com
            .silenteight
            .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder().build())
        .matches([])
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

  private static gnsRtRecommendationRequest(alerts) {
    new GnsRtRecommendationRequest(
        screenCustomerNameRes: new GnsRtScreenCustomerNameRes(
            screenCustomerNameResPayload: new GnsRtScreenCustomerNameResPayload(
                screenCustomerNameResInfo: new GnsRtScreenCustomerNameResInfo(
                    immediateResponseData: new ImmediateResponseData(
                        alerts: alerts
                    )
                )
            )
        ))
  }

}
