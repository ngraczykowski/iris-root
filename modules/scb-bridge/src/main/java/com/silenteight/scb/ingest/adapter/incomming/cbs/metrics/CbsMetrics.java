package com.silenteight.scb.ingest.adapter.incomming.cbs.metrics;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.AckCalledEvent;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.event.EventListener;

@Slf4j
class CbsMetrics implements MeterBinder {

  private static final String BASE_UNIT = "calls";
  private static final String TAG_STATUS = "status";
  private static final String TAG_LEVEL = "level";
  private static final String TAG_FUNCTION = "function";
  private static final String LEVEL_WATCHLIST = "watchlist";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    meterRegistry = registry;
  }

  @EventListener({
      AckCalledEvent.class,
      CbsCallFailedEvent.class,
      RecomCalledEvent.class,
  })
  public void cbsEventListener(Object event) {
    if (meterRegistry == null)
      return;

    if (event instanceof AckCalledEvent)
      onAckCalled((AckCalledEvent) event);
    else if (event instanceof RecomCalledEvent)
      onRecomCalled((RecomCalledEvent) event);
    else if (event instanceof CbsCallFailedEvent)
      onCallFailed((CbsCallFailedEvent) event);
  }

  private void onAckCalled(AckCalledEvent event) {
    incrementCounterWithTags(
        MetricNames.ACK_CALL,
        "Count of calls to ACK function.",
        TAG_STATUS, event.getStatusCode(),
        TAG_LEVEL, event.isWatchlistLevel() ? LEVEL_WATCHLIST : "alert");
  }

  private void onRecomCalled(RecomCalledEvent event) {
    incrementCounterWithTags(
        MetricNames.RECOM_CALL,
        "Count of calls to RECOM function.",
        TAG_STATUS, event.getStatusCode(),
        TAG_LEVEL, LEVEL_WATCHLIST);
  }

  private void onCallFailed(CbsCallFailedEvent event) {
    incrementCounterWithTags(
        MetricNames.CBS_CALL_ERROR,
        "Count of failed calls to Oracle function, i.e., calls that caused an exception.",
        TAG_FUNCTION, event.getFunctionType());
  }

  private void incrementCounterWithTags(String meterName, String description, String... tags) {
    Counter
        .builder(meterName)
        .baseUnit(BASE_UNIT)
        .description(description)
        .tags(tags)
        .register(meterRegistry)
        .increment();
  }

  static class MetricNames {

    static final String ACK_CALL = "serp.scb.bridge.cbs.function.ack";
    static final String RECOM_CALL = "serp.scb.bridge.cbs.function.recom";
    static final String CBS_CALL_ERROR = "serp.scb.bridge.cbs.function.error";

    private MetricNames() {
    }
  }
}
