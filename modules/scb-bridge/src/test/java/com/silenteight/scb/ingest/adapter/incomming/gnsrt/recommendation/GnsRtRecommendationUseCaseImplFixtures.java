package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsResponse;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts;

import static java.util.Arrays.asList;

class GnsRtRecommendationUseCaseImplFixtures {

  ObjectId alertId1 = ObjectId.newBuilder()
      .setSourceId("alertId1")
      .setDiscriminator("discriminator")
      .build();

  ObjectId alertId2 = ObjectId.newBuilder()
      .setSourceId("alertId2")
      .setDiscriminator("discriminator")
      .build();

  ObjectId unexpectedAlertId = ObjectId.newBuilder()
      .setSourceId("unexpectedAlertId")
      .setDiscriminator("discriminator")
      .build();

  GnsRtAlert gnsAlert1 = gnsAlert(alertId1);
  GnsRtRecommendationRequest requestForAlert1 = request(gnsAlert1);
  GnsRtAlert gnsAlert2 = gnsAlert(alertId2);
  GnsRtRecommendationRequest requestForAlert1AndAlert2 = request(gnsAlert1, gnsAlert2);

  Alert alert1 = alert(alertId1);
  AlertRecommendation recommendation1 = AlertRecommendation.newBuilder()
      .setAlertId(alertId1)
      .setAlert(alert1)
      .build();
  GenerateRecommendationsResponse response1 = GenerateRecommendationsResponse
      .newBuilder()
      .setAlertRecommendation(recommendation1)
      .build();
  Alert alert2 = alert(alertId2);
  AlertRecommendation recommendation2 = AlertRecommendation.newBuilder()
      .setAlertId(alertId2)
      .setAlert(alert2)
      .build();
  GenerateRecommendationsResponse response2 = GenerateRecommendationsResponse
      .newBuilder()
      .setAlertRecommendation(recommendation2)
      .build();
  Alert unexpectedAlert = alert(unexpectedAlertId);
  AlertRecommendation unexpectedRecommendation = AlertRecommendation.newBuilder()
      .setAlertId(unexpectedAlertId)
      .setAlert(unexpectedAlert)
      .build();
  GenerateRecommendationsResponse unexpectedResponse = GenerateRecommendationsResponse
      .newBuilder()
      .setAlertRecommendation(unexpectedRecommendation)
      .build();

  GnsRtResponseAlerts mappedResponse1 = createMappedResponse(alertId1);
  GnsRtResponseAlerts mappedResponse2 = createMappedResponse(alertId2);

  private static GnsRtRecommendationRequest request(GnsRtAlert... alerts) {
    ImmediateResponseData immediateResponseData = new ImmediateResponseData();
    immediateResponseData.setAlerts(asList(alerts));

    GnsRtScreenCustomerNameResInfo info = new GnsRtScreenCustomerNameResInfo();
    info.setImmediateResponseData(immediateResponseData);

    GnsRtScreenCustomerNameResPayload payload = new GnsRtScreenCustomerNameResPayload();
    payload.setScreenCustomerNameResInfo(info);

    GnsRtScreenCustomerNameRes screenCustomerNameRes = new GnsRtScreenCustomerNameRes();
    screenCustomerNameRes.setScreenCustomerNameResPayload(payload);

    GnsRtRecommendationRequest gnsRtRecommendationRequest = new GnsRtRecommendationRequest();
    gnsRtRecommendationRequest.setScreenCustomerNameRes(screenCustomerNameRes);

    return gnsRtRecommendationRequest;
  }

  private static GnsRtAlert gnsAlert(ObjectId id) {
    var alert = new GnsRtAlert();
    alert.setAlertId(id.getSourceId());
    alert.setWatchlistType("AM");
    return alert;
  }

  private static Alert alert(ObjectId id) {
    return Alert.newBuilder()
        .setId(id)
        .build();
  }

  private static GnsRtResponseAlerts createMappedResponse(ObjectId id) {
    var alerts = new GnsRtResponseAlerts();
    alerts.setAlertId(id.getSourceId());
    alerts.setWatchlistType("AM");
    return alerts;
  }
}
