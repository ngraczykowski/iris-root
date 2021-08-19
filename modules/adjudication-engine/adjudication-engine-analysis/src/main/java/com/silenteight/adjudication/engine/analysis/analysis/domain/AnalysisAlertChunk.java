package com.silenteight.adjudication.engine.analysis.analysis.domain;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnalysisAlertChunk {

  private final List<AnalysisAlert> analysisAlerts;

  public AnalysisAlertChunk(List<? extends AnalysisAlert> analysisAlerts) {
    this.analysisAlerts = new ArrayList<>(analysisAlerts);
  }

  public int getSize() {
    return analysisAlerts.size();
  }

  /**
   * Iterates over a list of {@link MissingMatchFeature}s sorted by agent config.
   */
  public void forEach(Consumer<AnalysisAlert> consumer) {
    analysisAlerts.forEach(consumer);
  }
}
