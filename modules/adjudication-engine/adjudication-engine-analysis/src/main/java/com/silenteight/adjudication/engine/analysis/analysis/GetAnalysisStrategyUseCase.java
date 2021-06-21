package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetAnalysisStrategyUseCase {

  private final AnalysisRepository analysisRepository;

  String getAnalysisStrategy(long analysisId) {
    return analysisRepository.getStrategyById(analysisId).getStrategy();
  }
}
