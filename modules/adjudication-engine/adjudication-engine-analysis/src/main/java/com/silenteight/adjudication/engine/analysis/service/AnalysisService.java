package com.silenteight.adjudication.engine.analysis.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class AnalysisService {

  @NonNull
  private final AnalysisFacade analysisFacade;

  Analysis createAnalysis(CreateAnalysisRequest request) {
    return analysisFacade.createAnalysis(request.getAnalysis());
  }
}
