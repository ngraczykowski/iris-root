package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.RawAlertService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUpdater;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Builder
@RequiredArgsConstructor
public class GnsRtRecommendationUseCaseImpl implements GnsRtRecommendationUseCase {

  private final GnsRtRequestToAlertMapper alertMapper;
  private final GnsRtResponseMapper responseMapper;
  private final AlertInfoService alertInfoService;
  private final StoreGnsRtRecommendationUseCase storeGnsRtRecommendationUseCase;
  private final RecommendationGatewayService recommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;
  private final RawAlertService rawAlertService;

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    var alerts = mapAlerts(request);
    logGnsRtRequest(alerts);

    var internalBatchId = InternalBatchIdGenerator.generate();
    rawAlertService.store(internalBatchId, alerts);

    var registrationResponse =
        alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, Priority.HIGH);

    //feed uds
    alerts.forEach(alert -> updateAndPublish(alert, registrationResponse));

    // TODO: wait for batch completed with batchId, then get the recommendations and use mapResponse

    return Mono.empty();
  }

  private void updateAndPublish(Alert alert, RegistrationResponse registrationResponse) {
    AlertUpdater.updatedWithRegistrationInfo(alert, registrationResponse);
    ingestEventPublisher.publish(alert);
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

  private List<Alert> mapAlerts(GnsRtRecommendationRequest request) {
    return alertMapper.map(request);
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
    return String.format("%s (%s hits)", alert.id().sourceId(), alert.getMatchesCount());
  }
}
