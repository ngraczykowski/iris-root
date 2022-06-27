package com.silenteight.simulator.processing.alert.index.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.processing.alert.index.domain.State;

@Value
@Builder
public class IndexedAlertDto {

  @NonNull
  String requestId;
  @NonNull
  String analysisName;
  @NonNull
  State state;
  long alertCount;
}
