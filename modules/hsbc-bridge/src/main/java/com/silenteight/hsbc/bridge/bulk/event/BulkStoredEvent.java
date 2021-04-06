package com.silenteight.hsbc.bridge.bulk.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BulkStoredEvent {

  private final String bulkId;
}
