package com.silenteight.simulator.management;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.model.api.v1.SolvingModel;

public interface AnalysisService {

  Analysis createAnalysis(@NonNull SolvingModel model);

  void addDatasetToAnalysis(@NonNull String analysis, @NonNull String dataset);

  Analysis getAnalysis(@NonNull String analysis);
}
