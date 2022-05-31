package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Repository
class JdbcAnalysisFeaturesFacade implements MatchFeaturesFacade {

  private final SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery;

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  @Override
  public Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    return selectAnalysisFeaturesQuery.findAlertAnalysisFeatures(analysis, alerts);
  }
}
