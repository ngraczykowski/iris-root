package com.silenteight.hsbc.bridge.bulk.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class BulkStoredEvent {

  private final UUID bulkId;
}
