package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetAnalysisStrategyUseCase {

  private final AnalysisRepository analysisRepository;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  String getAnalysisStrategy(long analysisId) {
    return analysisRepository.getStrategyById(analysisId).getStrategy();
  }
}
