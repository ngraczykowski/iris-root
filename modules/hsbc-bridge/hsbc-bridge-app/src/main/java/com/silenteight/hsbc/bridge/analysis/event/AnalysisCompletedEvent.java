package com.silenteight.hsbc.bridge.analysis.event;

import lombok.NonNull;
import lombok.Value;

@Value
public class AnalysisCompletedEvent {

  @NonNull String analysis;
}
