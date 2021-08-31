package com.silenteight.adjudication.engine.analysis.analysis.domain;

import lombok.Value;

@Value
public class AnalysisAlert {

  long analysisId;

  long alertId;

  public String getName() {
    return "analysis/" + analysisId + "/alerts/" + alertId;
  }
}
