package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisFacade {

  @NonNull
  private final CreateAnalysisUseCase createAnalysisUseCase;

  public List<Analysis> createAnalysis(Iterable<Analysis> analysis) {
    return createAnalysisUseCase.createAnalysis(analysis);
  }
}
