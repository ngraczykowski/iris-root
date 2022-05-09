package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import java.util.Objects;
import javax.validation.Validator;

@RequiredArgsConstructor
class GnsRtRecommendationRequestValidator {

  private final Validator validator;

  boolean isValid(GnsRtRecommendationRequest request) {
    var screenableData = getScreenableData(request);
    return isJsonValid(request) && isClientTypeNotNull(screenableData);
  }

  private boolean isJsonValid(GnsRtRecommendationRequest request) {
    var violations = validator.validate(request);
    return violations.isEmpty();
  }

  private boolean isClientTypeNotNull(ScreenableData screenableData) {
    return !Objects.isNull(screenableData.getClientType());
  }

  private ScreenableData getScreenableData(GnsRtRecommendationRequest request) {
    return request.getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo()
        .getScreenableData();
  }
}
