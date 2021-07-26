package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetAnalysisStrategyUseCase {

  private final AnalysisRepository analysisRepository;

  @Timed("ae.analysis.use_case.analysis.get_analysis_strategy")
  String getAnalysisStrategy(long analysisId) {
    return analysisRepository.getStrategyById(analysisId).getStrategy();
  }
}
