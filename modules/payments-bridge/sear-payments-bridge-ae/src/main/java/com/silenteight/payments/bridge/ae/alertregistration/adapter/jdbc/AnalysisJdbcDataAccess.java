package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
class AnalysisJdbcDataAccess implements AnalysisDataAccessPort {

  private final FindTodayAnalysisQuery findTodayAnalysisQuery;
  private final InsertAnalysisQuery insertAnalysisQuery;
  private final ExistsAnalysisQuery existsAnalysisQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public Optional<String> findCurrentAnalysis() {
    return findTodayAnalysisQuery.execute();
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public Optional<String> save(String analysisName) {
    return insertAnalysisQuery.update(analysisName);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public boolean existsAnalysis(String analysisName) {
    var exist = existsAnalysisQuery.execute(analysisName);
    if (log.isDebugEnabled() && !exist) {
      log.debug("Analysis {} not found inside pb - ignore", analysisName);
    }
    return exist;
  }
}
