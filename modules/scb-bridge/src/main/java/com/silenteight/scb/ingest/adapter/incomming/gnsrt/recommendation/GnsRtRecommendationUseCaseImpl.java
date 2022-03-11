package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.scb.ingest.adapter.outgoing.RegistrationApiClient;
import com.silenteight.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

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
  private final RegistrationApiClient registrationApiClient;

  @Override
  public Mono<GnsRtRecommendationResponse> recommend(@NonNull GnsRtRecommendationRequest request) {
    List<Alert> alerts = mapAlerts(request);
    logGnsRtRequest(alerts);

    //register batch (what is batch in gns-rt?)
    //register alerts & matches
    RegistrationResponse registrationResponse =
        registrationApiClient.registerAlertsAndMatches(RegistrationRequest.builder().build());

    return Mono.empty();
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
