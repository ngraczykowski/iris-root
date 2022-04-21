package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.HIGH;
import static com.silenteight.scb.ingest.domain.model.BatchSource.GNS_RT;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GnsRtRecommendationUseCaseImpl implements GnsRtRecommendationUseCase {

  private final GnsRtRequestToAlertMapper alertMapper;
  private final GnsRtResponseMapper responseMapper;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  private final RawAlertService rawAlertService;
  private final BatchInfoService batchInfoService;
  private final GnsRtRecommendationService gnsRtRecommendationService;
  private final TrafficManager trafficManager;

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    trafficManager.activateRtSemaphore();
    var internalBatchId = InternalBatchIdGenerator.generate();

    var alerts = mapAlerts(request, internalBatchId);
    logGnsRtRequest(alerts, internalBatchId);

    rawAlertService.store(internalBatchId, alerts);
    batchInfoService.store(internalBatchId, GNS_RT, alerts.size());

    var registrationBatchContext = new RegistrationBatchContext(HIGH, GNS_RT);
    var registrationResponse =
        alertRegistrationFacade
            .registerSolvingAlerts(internalBatchId, alerts, registrationBatchContext);

    feedUds(alerts, registrationResponse);

    return gnsRtRecommendationService.recommendationsMono(internalBatchId)
        .map(recommendations -> mapResponse(request, recommendations));
  }

  private void feedUds(List<Alert> alerts, RegistrationResponse registrationResponse) {
    alerts.forEach(alert -> updateAndPublish(alert, registrationResponse));
  }

  private void updateAndPublish(Alert alert, RegistrationResponse registrationResponse) {
    AlertUpdater.updatedWithRegistrationInfo(alert, registrationResponse);
    ingestEventPublisher.publish(alert);
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
