package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;

import static java.util.Arrays.asList;

class GnsRtRecommendationUseCaseImplFixtures {

  ObjectId alertId1 = ObjectId.builder()
      .sourceId("alertId1")
      .discriminator("discriminator")
      .build();

  ObjectId alertId2 = ObjectId.builder()
      .sourceId("alertId2")
      .discriminator("discriminator")
      .build();

  GnsRtAlert gnsAlert1 = gnsAlert(alertId1);
  GnsRtRecommendationRequest requestForAlert1 = request(gnsAlert1);
  GnsRtAlert gnsAlert2 = gnsAlert(alertId2);
  GnsRtRecommendationRequest requestForAlert1AndAlert2 = request(gnsAlert1, gnsAlert2);

  Alert alert1 = alert(alertId1);
  Alert alert2 = alert(alertId2);

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
    alert.setAlertId(id.sourceId());
    alert.setWatchlistType("AM");
    return alert;
  }

  private static Alert alert(ObjectId id) {
    return Alert.builder()
        .id(id)
        .build();
  }
}
