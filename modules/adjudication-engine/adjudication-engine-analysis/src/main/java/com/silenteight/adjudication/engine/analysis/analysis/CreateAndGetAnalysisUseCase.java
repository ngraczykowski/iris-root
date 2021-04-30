package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CreateAndGetAnalysisUseCase {

  @NonNull
  private final CreateAnalysisUseCase createAnalysisUseCase;

  @NonNull
  private final GetAnalysisUseCase getAnalysisUseCase;

  Analysis createAndGetAnalysis(Analysis prototype) {
    var analysisName = createAnalysisUseCase.createAnalysis(prototype);
    return getAnalysisUseCase.getAnalysis(analysisName);
  }
}
