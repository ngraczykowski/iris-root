package com.silenteight.hsbc.bridge.analysis.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateAnalysisEvent {

  private final String analysisName;
  private final String datasetName;
  private final String solvingModelName;
}
