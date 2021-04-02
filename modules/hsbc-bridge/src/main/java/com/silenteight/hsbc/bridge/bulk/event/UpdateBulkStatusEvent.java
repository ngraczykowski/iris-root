package com.silenteight.hsbc.bridge.bulk.event;

import lombok.Value;

import java.util.UUID;

@Value
public class UpdateBulkStatusEvent {

  UUID bulkId;
}
