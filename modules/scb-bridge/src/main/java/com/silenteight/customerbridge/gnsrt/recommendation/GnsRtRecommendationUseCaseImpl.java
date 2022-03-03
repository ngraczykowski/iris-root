package com.silenteight.customerbridge.gnsrt.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.customerbridge.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.customerbridge.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtResponseAlerts;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Builder
public class GnsRtRecommendationUseCaseImpl implements GnsRtRecommendationUseCase {

  private final GnsRtRequestToAlertMapper alertMapper;
  private final GnsRtResponseMapper responseMapper;
  private final AlertInfoService alertInfoService;
  private final StoreGnsRtRecommendationUseCase storeGnsRtRecommendationUseCase;
  private final RecommendationGatewayService recommendationService;

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    List<Alert> alerts = mapAlerts(request);
    logGnsRtRequest(alerts);

    return sendAlertInfo(alerts)
        .thenMany(recommendAlerts(alerts))
        .doOnNext(this::storeResponse)
        .map(response -> mapResponse(request, response))
        .collect(
            GnsRtRecommendationResponse::new,
            (response, alert) -> response.getSilent8Response().getAlerts().add(alert));
  }

  private void storeResponse(GenerateRecommendationsResponse response) {
    storeGnsRtRecommendationUseCase.storeRecommendation(response.getAlertRecommendation());
  }

  private List<Alert> mapAlerts(GnsRtRecommendationRequest request) {
    return alertMapper.map(request);
  }

  private Mono<Void> sendAlertInfo(List<Alert> alerts) {
    return alertInfoService.sendAlertInfo(alerts);
  }

  private Flux<GenerateRecommendationsResponse> recommendAlerts(List<Alert> alerts) {
    return Flux.defer(() -> recommendationService.recommend(alerts));
  }

  private GnsRtResponseAlerts mapResponse(
      GnsRtRecommendationRequest request, GenerateRecommendationsResponse response) {

    var recommendation = response.getAlertRecommendation();
    var sourceId = recommendation.getAlertId().getSourceId();
    var gnsAlert = GnsRtAlertResolver.resolve(request, sourceId);

    return responseMapper.map(gnsAlert, recommendation);
  }

  private static void logGnsRtRequest(List<Alert> alerts) {
    if (log.isInfoEnabled()) {
      String alertsMsg = alerts
          .stream()
          .map(GnsRtRecommendationUseCaseImpl::prepareAlertMessage)
          .collect(Collectors.joining(", "));
      log.info("Received GNS-RT Request, alerts=[{}]", alertsMsg);
    }
  }

  private static String prepareAlertMessage(Alert alert) {
    return String.format("%s (%s hits)", alert.getId().getSourceId(), alert.getMatchesCount());
  }
}

