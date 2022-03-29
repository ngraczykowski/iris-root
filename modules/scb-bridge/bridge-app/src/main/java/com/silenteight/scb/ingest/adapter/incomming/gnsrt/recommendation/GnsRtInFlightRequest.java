package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import org.apache.commons.lang3.time.StopWatch;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
@ToString
@RequiredArgsConstructor
public class GnsRtInFlightRequest {

  @ToString.Exclude
  private final CompletableFuture<Recommendations> future = new CompletableFuture<>();

  @Getter
  private final String internalBatchId;

  private final StopWatch stopWatch = StopWatch.createStarted();

  public void recommendationsReceived(Recommendations recommendations) {
    log.info("Recommendations are available for {}", this);
    future.complete(recommendations);
  }

  public void batchFailed(String errorDescription) {
    future.completeExceptionally(
        new RuntimeException(String.format("Batch with internalBatchId: %s failed, reason: %s",
            internalBatchId, errorDescription)));
  }

  public Mono<Recommendations> mono() {
    return Mono.fromFuture(future);
  }

}