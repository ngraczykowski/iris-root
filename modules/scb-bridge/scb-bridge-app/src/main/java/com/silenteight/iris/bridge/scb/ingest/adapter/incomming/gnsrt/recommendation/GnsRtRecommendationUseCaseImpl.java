/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource.GNS_RT;
import static com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext.GNS_RT_CONTEXT;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GnsRtRecommendationUseCaseImpl implements GnsRtRecommendationUseCase {

  private final GnsRtRequestToAlertMapper alertMapper;
  private final GnsRtResponseMapper responseMapper;
  private final BatchAlertIngestService ingestService;
  private final RawAlertService rawAlertService;
  private final BatchInfoService batchInfoService;
  private final GnsRtRecommendationService gnsRtRecommendationService;
  private final TrafficManager trafficManager;
  private final GnsRtRecommendationProperties recommendationProperties;
  private final Scheduler scheduler;

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    trafficManager.activateRtSemaphore();

    return Mono.fromCallable(InternalBatchIdGenerator::generate)
        .publishOn(scheduler)
        .flatMap(internalBatchId -> {
          registerRequest(request, internalBatchId);
          return gnsRtRecommendationService.recommendationsMono(internalBatchId);
        })
        .timeout(
            Duration.ofSeconds(recommendationProperties.getDeadlineInSeconds()),
            Mono.fromCallable(
                () -> {
                  log.info(
                      "Gns-RT solving timeout set to {} has expired. "
                          + "Generating the Manual:Investigation response",
                      recommendationProperties.getDeadlineInSeconds());
                  return GnsRtManualInvestigationRecomBuilder
                      .prepareManualInvestigationRecommendation(request);
                }))
        .map(recommendations -> mapResponse(request, recommendations));
  }

  private void registerRequest(GnsRtRecommendationRequest request, String internalBatchId) {
    var alerts = mapAlerts(request, internalBatchId);
    logGnsRtRequest(alerts, internalBatchId);

    rawAlertService.store(internalBatchId, alerts);
    batchInfoService.store(internalBatchId, GNS_RT, alerts.size());

    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, GNS_RT_CONTEXT);
  }

  private List<Alert> mapAlerts(GnsRtRecommendationRequest request, String internalBatchId) {
    return alertMapper.map(request, internalBatchId);
  }

  private static void logGnsRtRequest(List<Alert> alerts, String internalBatchId) {
    if (log.isInfoEnabled()) {
      String alertsMsg = alerts
          .stream()
          .map(GnsRtRecommendationUseCaseImpl::prepareAlertMessage)
          .collect(Collectors.joining(", "));
      log.info("Received GNS-RT Request, internalBatchId: {}, alerts: [{}]", internalBatchId,
          alertsMsg);
    }
  }

  private static String prepareAlertMessage(Alert alert) {
    return String.format("%s (%s hits)", alert.id().sourceId(), alert.getMatchesCount());
  }

  private GnsRtRecommendationResponse mapResponse(
      GnsRtRecommendationRequest request, Recommendations recommendations) {

    var alerts = recommendations
        .recommendations()
        .stream()
        .map(recommendation -> map(request, recommendation))
        .toList();

    var response = new GnsRtRecommendationResponse();
    response.getSilent8Response().setAlerts(alerts);
    return response;
  }

  private GnsRtResponseAlert map(
      GnsRtRecommendationRequest request, Recommendations.Recommendation recommendation) {

    var sourceId = recommendation.alert().id();
    var gnsRtAlert = GnsRtAlertResolver.resolve(request, sourceId);

    return responseMapper.map(gnsRtAlert, recommendation);
  }
}
