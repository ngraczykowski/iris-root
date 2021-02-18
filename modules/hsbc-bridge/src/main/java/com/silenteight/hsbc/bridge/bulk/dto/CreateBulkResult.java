package com.silenteight.hsbc.bridge.bulk.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CreateBulkResult {
  UUID bulkId;
  List<BulkItem> bulkItems;
}
