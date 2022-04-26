package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Slf4j
public class ReactiveAlertInFlightService {

  private final AlertInFlightService delegate;

  public Mono<Void> delete(@NonNull AlertId alertId) {
    return Mono.defer(() -> Mono
        .fromRunnable(() -> {
          try {
            delegate.delete(alertId);
            if (log.isDebugEnabled())
              log.debug("Removed alert under processing: alertId={}", alertId);
          } catch (Exception e) {
            log.warn(
                "Failed to delete information about alert under processing: alertId={}", alertId,
                e);
          }
        })
        .publishOn(Schedulers.boundedElastic())
        .then()
    );
  }

  public Mono<Void> update(
      @NonNull AlertId alertId, @NonNull AlertUnderProcessing.State state, @NonNull String error) {
    return Mono.defer(() -> Mono
        .fromRunnable(() -> {
          try {
            delegate.error(alertId, error);
            if (log.isDebugEnabled())
              log.debug("Updated alert under processing: alertId={}, state={}", alertId, state);
          } catch (Exception e) {
            log.warn("Failed to update state of alert under processing: alertId={}", alertId, e);
          }
        })
        .publishOn(Schedulers.boundedElastic())
        .then()
    );
  }
}
