package com.silenteight.hsbc.bridge.bulk.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.bulk.BulkStatus;

@Builder
@Value
public class UpdateBulkItemStatusEvent {

  long bulkItemId;
  @NonNull BulkStatus newStatus;
}
