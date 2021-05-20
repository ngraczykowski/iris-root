package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisDataAccess;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcAnalysisDataAccess implements AnalysisDataAccess {

  private final SelectAnalysisAgentConfigQuery selectAnalysisAgentConfigQuery;

  @Override
  public List<String> findAgentConfigsByAnalysisId(long analysisId) {
    return selectAnalysisAgentConfigQuery.execute(analysisId);
  }
}
