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
  private final CreateAndGetAnalysisUseCase createAndGetAnalysisUseCase;

  @NonNull
  private final GetAnalysisUseCase getAnalysisUseCase;

  @NonNull
  private final AddAndListDatasetsInAnalysisUseCase addAndListDatasetsInAnalysisUseCase;

  public Analysis createAndGetAnalysis(Analysis analysis) {
    return createAndGetAnalysisUseCase.createAndGetAnalysis(analysis);
  }

  public List<AnalysisDataset> addDatasets(String analysis, List<String> datasets) {
    return addAndListDatasetsInAnalysisUseCase.addAndListDatasets(analysis, datasets);
  }

  public Analysis getAnalysis(String analysisName) {
    return getAnalysisUseCase.getAnalysis(analysisName);
  }
}
