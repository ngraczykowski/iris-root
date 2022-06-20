/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;

import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@SuperBuilder
public abstract class RecordCompositeWriter implements ItemWriter<AlertComposite> {

  private final GnsSyncDeltaService deltaService;
  private final boolean useDelta;
  private final String deltaJobName;

  @Override
  public void write(List<? extends AlertComposite> items) {
    writeAlerts(items);

    if (useDelta)
      deltaService.updateDelta(getDeltas(items), deltaJobName);

    if (log.isDebugEnabled())
      log.debug("Saved records: count={}", items.size());
  }

  protected abstract void writeAlerts(List<? extends AlertComposite> items);

  private static Map<String, Integer> getDeltas(List<? extends AlertComposite> items) {
    var alertCollector = toMap(
        AlertComposite::getSystemId, AlertComposite::getDecisionsCount,
        (existing, replacement) -> existing);

    return items
        .stream()
        .collect(alertCollector);
  }
}
