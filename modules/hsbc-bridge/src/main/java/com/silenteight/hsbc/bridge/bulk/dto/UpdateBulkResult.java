package com.silenteight.hsbc.bridge.bulk.dto;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.hsbc.bridge.bulk.BulkStatus;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class UpdateBulkResult {
  UUID bulkId;
  BulkStatus bulkStatus;
  List<BulkItem> bulkItems;
}
