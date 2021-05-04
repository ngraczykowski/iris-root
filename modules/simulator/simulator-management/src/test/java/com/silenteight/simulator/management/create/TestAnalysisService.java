package com.silenteight.simulator.management.create;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.model.api.v1.SolvingModel;

import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS;

public class TestAnalysisService implements AnalysisService {

  @Override
  public Analysis createAnalysis(SolvingModel model) {
    return ANALYSIS;
  }

  @Override
  public void addDatasetToAnalysis(String analysis, String dataset) {

  }

  @Override
  public Analysis getAnalysis(String analysis) {
    return ANALYSIS;
  }
}
