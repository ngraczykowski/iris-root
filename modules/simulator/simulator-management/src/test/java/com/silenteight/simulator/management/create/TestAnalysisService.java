package com.silenteight.simulator.management.create;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.management.SimulationFixtures;

import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS_NAME_3;
import static com.silenteight.simulator.management.SimulationFixtures.MAP_OF_ANALYSIS_WITH_NAMES;

public class TestAnalysisService implements AnalysisService {

  @Override
  public Analysis createAnalysis(SolvingModel model) {
    return SimulationFixtures.createAnalysis(ANALYSIS_NAME_3, 0);
  }

  @Override
  public void addDatasetToAnalysis(String analysis, String dataset) {

  }

  @Override
  public Analysis getAnalysis(String analysis) {
    return MAP_OF_ANALYSIS_WITH_NAMES.get(analysis);
  }
}
