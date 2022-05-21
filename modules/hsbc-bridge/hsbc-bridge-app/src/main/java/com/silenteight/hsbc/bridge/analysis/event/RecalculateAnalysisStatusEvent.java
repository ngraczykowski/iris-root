package com.silenteight.hsbc.bridge.analysis.event;

import lombok.Value;

@Value
public class RecalculateAnalysisStatusEvent {

  String analysisName;
}
