package com.silenteight.hsbc.bridge.bulk;

import lombok.Value;

import com.silenteight.hsbc.bridge.rest.model.input.SolvedAlert;

import java.util.List;
import java.util.UUID;

@Value
public class BulkProcessingResult {
  UUID bulkId;
  BulkStatus bulkStatus;
  List<SolvedAlert> solvedAlerts;
}
