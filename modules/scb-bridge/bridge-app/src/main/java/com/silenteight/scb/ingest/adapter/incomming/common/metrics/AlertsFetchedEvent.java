package com.silenteight.scb.ingest.adapter.incomming.common.metrics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AlertsFetchedEvent {

  private final int chunkSize;
}
