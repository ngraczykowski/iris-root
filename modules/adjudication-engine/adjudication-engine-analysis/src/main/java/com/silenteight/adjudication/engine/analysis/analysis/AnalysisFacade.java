package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnalysisFacade {

  @NonNull
  private final CreateAnalysisUseCase createAnalysisUseCase;

  public Analysis createAnalysis(Analysis analysis) {
    return createAnalysisUseCase.createAnalysis(analysis);
  }
}
