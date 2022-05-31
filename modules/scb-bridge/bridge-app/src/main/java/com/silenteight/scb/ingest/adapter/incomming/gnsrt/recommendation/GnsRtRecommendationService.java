package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GnsRtRecommendationService {

  private final GnsRtInFlightRequestsRegistry registry;

  public Mono<Recommendations> recommendationsMono(String internalBatchId) {
    var inFlightRequest = new GnsRtInFlightRequest(internalBatchId);

    var mono = inFlightRequest.mono()
        .doOnCancel(() -> log.info("GNS-RT Client has cancelled: {}", inFlightRequest))
        .doOnError(e -> log.error("Exception during processing GNS-RT {}", inFlightRequest, e))
        .doOnSuccess(__ -> log.info("GNS-RT {} finished successfully", inFlightRequest))
        .doFinally(__ -> registry.unregister(inFlightRequest));

    registry.register(inFlightRequest);

    return mono;
  }

  public void recommend(List<Recommendation> recommendations) {
    registry.recommendationsReceived(
        getBatchId(recommendations),
        Recommendations.builder()
            .recommendations(recommendations)
            .build());
  }

  private static String getBatchId(List<Recommendation> recommendations) {
    if (recommendations.isEmpty()) {
      throw new IllegalStateException("Empty recommendations");
    }
    return recommendations.get(0).batchId();
  }

  public void batchFailed(String internalBatchId, String errorDescription) {
    registry.batchFailed(internalBatchId, errorDescription);
  }

}
