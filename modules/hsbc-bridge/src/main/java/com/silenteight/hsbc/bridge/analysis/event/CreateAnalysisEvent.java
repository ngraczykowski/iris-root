package com.silenteight.hsbc.bridge.analysis.event;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@Value
public class CreateAnalysisEvent {

  String analysisName;
  String datasetName;
  String solvingModelName;
}
