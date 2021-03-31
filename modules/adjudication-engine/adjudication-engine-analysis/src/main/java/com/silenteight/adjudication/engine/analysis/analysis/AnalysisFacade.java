package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisDataset;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisFacade {

  @NonNull
  private final CreateAnalysisUseCase createAnalysisUseCase;

  @NonNull
  private final AddDatasetsToAnalysisUseCase addDatasetsToAnalysisUseCase;

  public Analysis createAnalysis(Analysis analysis) {
    return createAnalysisUseCase.createAnalysis(analysis);
  }

  public List<AnalysisDataset> addDatasets(String analysis, List<String> datasets) {
    return addDatasetsToAnalysisUseCase.addDatasets(analysis, datasets);
  }
}
