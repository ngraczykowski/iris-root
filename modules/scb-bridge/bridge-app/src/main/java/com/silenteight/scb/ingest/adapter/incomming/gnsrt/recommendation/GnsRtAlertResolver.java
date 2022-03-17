package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;

import static java.util.Optional.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GnsRtAlertResolver {

  static GnsRtAlert resolve(
      GnsRtRecommendationRequest request, String sourceId) {

    return of(request)
        .map(GnsRtRecommendationRequest::getScreenCustomerNameRes)
        .map(GnsRtScreenCustomerNameRes::getScreenCustomerNameResPayload)
        .map(GnsRtScreenCustomerNameResPayload::getScreenCustomerNameResInfo)
        .map(GnsRtScreenCustomerNameResInfo::getImmediateResponseData)
        .map(ImmediateResponseData::getAlerts)
        .flatMap(l -> l.stream().filter(a -> a.getAlertId().equalsIgnoreCase(sourceId)).findFirst())
        .orElseThrow(() ->
            new IllegalStateException("Cannot determine gns alert for an alert: " + sourceId));
  }
}
