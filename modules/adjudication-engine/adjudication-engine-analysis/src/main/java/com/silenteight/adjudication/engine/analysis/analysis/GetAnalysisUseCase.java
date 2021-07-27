package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class GetAnalysisUseCase {

  private final AnalysisQueryRepository repository;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  @Transactional(readOnly = true)
  Analysis getAnalysis(String analysisName) {
    long analysisId = ResourceName.create(analysisName).getLong("analysis");

    return repository
        .findById(analysisId)
        .map(AnalysisQuery::toAnalysis)
        .orElseThrow(() -> new EntityNotFoundException(analysisName));
  }
}
