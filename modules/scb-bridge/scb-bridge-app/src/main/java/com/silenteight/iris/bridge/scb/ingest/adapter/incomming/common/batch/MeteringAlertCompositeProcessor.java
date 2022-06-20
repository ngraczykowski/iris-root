/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.batch.item.ItemProcessor;

public class MeteringAlertCompositeProcessor
    implements ItemProcessor<AlertComposite, AlertComposite>, MeterBinder {

  private MeterRegistry registry;

  @Override
  public AlertComposite process(AlertComposite item) {
    if (registry != null)
      registry.counter("serp.scb.bridge.alerts", item.getMeasuringTags()).increment();

    return item;
  }

  @Override
  public void bindTo(MeterRegistry registry) {
    this.registry = registry;
  }
}
