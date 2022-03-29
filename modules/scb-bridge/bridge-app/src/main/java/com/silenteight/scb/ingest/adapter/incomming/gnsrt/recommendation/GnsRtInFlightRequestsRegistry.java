package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

@Slf4j
@Component
public class GnsRtInFlightRequestsRegistry {

  private final ConcurrentMap<String, GnsRtInFlightRequest> pending = new ConcurrentHashMap<>();

  public void register(GnsRtInFlightRequest inFlightRequest) {
    log.info(
        "Adding {}. There are currently {} in flight GNS-RT requests", inFlightRequest,
        pending.size());

    pending.compute(inFlightRequest.getInternalBatchId(), putIfNotExists(inFlightRequest));
  }

  private <K, V> BiFunction<K, V, V> putIfNotExists(V value) {
    return (key, existingValue) -> {
      if (existingValue != null) {
        throw new IllegalStateException("Can't put as key: " + key + " is already used");
      }
      return value;
    };
  }

  public void unregister(GnsRtInFlightRequest inFlightRequest) {
    log.info(
        "Removing {}. There are currently {} in flight GNS-RT requests", inFlightRequest,
        pending.size());
    pending.remove(inFlightRequest.getInternalBatchId());
  }

  public void recommendationsReceived(String internalBatchId, Recommendations recommendations) {
    GnsRtInFlightRequest inFlightRequest = pending.get(internalBatchId);
    if (isExists(inFlightRequest)) {
      inFlightRequest.recommendationsReceived(recommendations);
    } else {
      log.info("Recommendations for Alerts with internalBatchId: {} are available but GNS-RT "
          + "Client no longer waits for them", internalBatchId);
    }
  }

  public void batchFailed(String internalBatchId, String errorDescription) {
    GnsRtInFlightRequest inFlightRequest = pending.get(internalBatchId);
    if (isExists(inFlightRequest)) {
      inFlightRequest.batchFailed(errorDescription);
    } else {
      log.error("Batch with internalBatchId: {} failed but GNS-RT Client no longer waits for "
          + "Recommendations", internalBatchId);
    }
  }

  private boolean isExists(@Nullable GnsRtInFlightRequest inFlightRequest) {
    return inFlightRequest != null;
  }

}
