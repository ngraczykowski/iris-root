package com.silenteight.adjudication.engine.analysis.analysis.domain;

import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

public class AnalysisAlertChunk {

  private final List<AnalysisAlert> analysisAlerts;

  public AnalysisAlertChunk(List<? extends AnalysisAlert> analysisAlerts) {
    this.analysisAlerts = new ArrayList<>(analysisAlerts);
  }

  public Optional<AnalysisAlertsAdded> toAnalysisAlertsAdded() {
    if (analysisAlerts.isEmpty())
      return empty();

    return Optional.of(AnalysisAlertsAdded.newBuilder()
        .addAllAnalysisAlerts(() -> analysisAlerts.stream().map(AnalysisAlert::getName).iterator())
        .build());
  }
}
