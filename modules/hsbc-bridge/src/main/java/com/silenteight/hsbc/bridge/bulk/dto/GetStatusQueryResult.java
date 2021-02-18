package com.silenteight.hsbc.bridge.bulk.dto;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.hsbc.bridge.bulk.BulkStatus;

import java.util.List;

@Builder
@Getter
public class GetStatusQueryResult {
  BulkStatus bulkStatus;
  List<BulkItem> bulkItems;
}
