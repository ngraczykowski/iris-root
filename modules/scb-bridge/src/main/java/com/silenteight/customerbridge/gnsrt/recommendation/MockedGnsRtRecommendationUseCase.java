package com.silenteight.customerbridge.gnsrt.recommendation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.customerbridge.gnsrt.model.request.GnsRtAlert;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtResponseAlerts;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum;

import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;

class MockedGnsRtRecommendationUseCase implements GnsRtRecommendationUseCase {

  private static final String COMMENT_TEMPLATE =
      "THIS IS A STUB RECOMMENDATION FOR ALERT <system_id>, DO NOT USE FOR PRODUCTION.";

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    validate(request);

    GnsRtRecommendationResponse response = new GnsRtRecommendationResponse();

    request
        .getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo()
        .getImmediateResponseData()
        .getAlerts()
        .stream()
        .filter(GnsRtAlert::isPotentialMatch)
        .map(MockedGnsRtRecommendationUseCase::createAlertRecommendation)
        .forEach(a -> response.getSilent8Response().getAlerts().add(a));

    return Mono.just(response);
  }

  private static GnsRtResponseAlerts createAlertRecommendation(GnsRtAlert alert) {
    GnsRtResponseAlerts responseAlert = new GnsRtResponseAlerts();
    String alertId = alert.getAlertId();
    responseAlert.setAlertId(alertId);
    responseAlert.setComments(COMMENT_TEMPLATE.replace("<system_id>", alertId));
    responseAlert.setRecommendationTimestamp(LocalDateTime.now());
    responseAlert.setRecommendation(RecommendationGenerator.generateRandomRecommendation());
    responseAlert.setWatchlistType(alert.getWatchlistType());
    return responseAlert;
  }

  private static void validate(GnsRtRecommendationRequest request) {
    if (hasNoPotentialMatches(request))
      throw new InvalidGnsRtRequestDataException("No Alerts with status POTENTIAL_MATCH.");
  }

  private static boolean hasNoPotentialMatches(GnsRtRecommendationRequest request) {
    return request
        .getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo()
        .getImmediateResponseData()
        .getAlerts()
        .stream()
        .noneMatch(GnsRtAlert::isPotentialMatch);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static class RecommendationGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    static RecommendationEnum generateRandomRecommendation() {
      int randomPosition = RANDOM.nextInt(RecommendationEnum.values().length);
      return RecommendationEnum.values()[randomPosition];
    }
  }
}
