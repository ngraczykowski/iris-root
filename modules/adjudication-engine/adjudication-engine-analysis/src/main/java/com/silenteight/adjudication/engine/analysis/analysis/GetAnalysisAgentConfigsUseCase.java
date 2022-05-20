package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetAnalysisAgentConfigsUseCase {

  private final AnalysisDataAccess dataAccess;

  @Transactional(readOnly = true)
  List<String> getAnalysisAgentConfigs(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    return dataAccess.findAgentConfigsByAnalysisId(analysisId);
  }
}
