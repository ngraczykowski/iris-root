package com.silenteight.adjudication.engine.analysis.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class AnalysisService {

  @NonNull
  private final AnalysisFacade analysisFacade;

  Analysis createAnalysis(CreateAnalysisRequest request) {
    return analysisFacade.createAnalysis(List.of(request.getAnalysis())).get(0);
  }
}
