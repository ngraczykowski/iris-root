package com.silenteight.hsbc.bridge.bulk.event;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;

@AllArgsConstructor
@Value
public class BulkProcessingStartedEvent {

  @NonNull String bulkId;
  @NonNull AnalysisDto analysis;
}
