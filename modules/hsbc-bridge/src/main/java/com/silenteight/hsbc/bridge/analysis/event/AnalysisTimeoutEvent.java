package com.silenteight.hsbc.bridge.analysis.event;

import lombok.Value;

@Value
public class AnalysisTimeoutEvent {

  long analysisId;
}
