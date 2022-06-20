/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert.RecommendationEnum;

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

  private static GnsRtResponseAlert createAlertRecommendation(GnsRtAlert alert) {
    String alertId = alert.getAlertId();
    return GnsRtResponseAlert.builder()
        .alertId(alertId)
        .comments(COMMENT_TEMPLATE.replace("<system_id>", alertId))
        .recommendationTimestamp(LocalDateTime.now())
        .recommendation(RecommendationGenerator.generateRandomRecommendation())
        .watchlistType(alert.getWatchlistType())
        .build();
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
