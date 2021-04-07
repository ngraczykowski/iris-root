package com.silenteight.simulator.management;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.model.api.v1.SolvingModel;

import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS;

class TestAnalysisService implements AnalysisService {

  @Override
  public Analysis createAnalysis(SolvingModel model) {
    return ANALYSIS;
  }

  @Override
  public void addDatasetToAnalysis(String analysisName, String datasetName) {

  }

  @Override
  public Analysis getAnalysis(String analysisName) {
    return ANALYSIS;
  }
}
