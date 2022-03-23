package com.silenteight.scb.ingest.adapter.incomming.cbs.metrics;

import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;

@Slf4j
public class CbsOracleMetrics implements MeterBinder {

  public static final String METRIC_FETCH_PREFIX = "serp.scb.bridge.oracle.fetch";

  public static final String SOURCE_VIEW_TAG_KEY = "source_view";
  public static final String CBS_HIT_DETAILS_VIEW_TAG_KEY = "cbs_hit_details_view";

  private MeterRegistry meterRegistry;

  @Override
  public void bindTo(MeterRegistry registry) {
    meterRegistry = registry;
  }

  public Timer recordReaderTimer(String sourceView) {
    return Timer.builder(METRIC_FETCH_PREFIX + ".record-reader.time")
        .tag(SOURCE_VIEW_TAG_KEY, sourceView)
        .register(meterRegistry);
  }

  public Timer decisionsReaderTimer(String sourceView) {
    return Timer.builder(METRIC_FETCH_PREFIX + ".decisions-reader.time")
        .tag(SOURCE_VIEW_TAG_KEY, sourceView)
        .register(meterRegistry);
  }

  public Timer cbsHitDetailsReaderTimer(String sourceView) {
    return Timer.builder(METRIC_FETCH_PREFIX + ".cbs-hit-details-reader.time")
        .tag(SOURCE_VIEW_TAG_KEY, sourceView)
        .register(meterRegistry);
  }

  public Timer alertRecordCompositeCollectionReaderTimer(String sourceView,
      String cbsHitDetailsView) {
    return Timer.builder(
        METRIC_FETCH_PREFIX + ".alert-record-composite-collection-reader.time")
        .tag(SOURCE_VIEW_TAG_KEY, sourceView)
        .tag(CBS_HIT_DETAILS_VIEW_TAG_KEY, cbsHitDetailsView)
        .register(meterRegistry);
  }

}
