package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
class JdbcMatchFeaturesDataAccess implements MatchFeatureDataAccess {
  private final SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery;

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    return selectAnalysisFeaturesQuery.findAlertAnalysisFeatures(analysis, alerts);
  }
}
