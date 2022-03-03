package com.silenteight.customerbridge.common.metrics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AlertsFetchedEvent {

  private final int chunkSize;
}
