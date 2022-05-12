package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import org.apache.commons.lang3.time.StopWatch;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

@Slf4j
@ToString
@RequiredArgsConstructor
public class GnsRtInFlightRequest {

  @ToString.Exclude
  private final Sinks.One<Recommendations> sink = Sinks.one();

  @Getter
  private final String internalBatchId;

  private final StopWatch stopWatch = StopWatch.createStarted();

  public void recommendationsReceived(Recommendations recommendations) {
    log.info("Recommendations are available for {}", this);
    sink.tryEmitValue(recommendations);
  }

  public void batchFailed(String errorDescription) {
    sink.emitError(
        new RuntimeException(String.format("Batch with internalBatchId: %s failed, reason: %s",
            internalBatchId, errorDescription)), EmitFailureHandler.FAIL_FAST);
  }

  public Mono<Recommendations> mono() {
    return sink.asMono();
  }

}