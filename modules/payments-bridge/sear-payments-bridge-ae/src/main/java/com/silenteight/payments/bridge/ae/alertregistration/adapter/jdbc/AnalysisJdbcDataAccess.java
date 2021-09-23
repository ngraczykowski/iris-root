package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class AnalysisJdbcDataAccess implements AnalysisDataAccessPort {

  private final FindTodayAnalysisQuery findTodayAnalysisQuery;
  private final InsertAnalysisQuery insertAnalysisQuery;

  @Override
  public Optional<Long> findTodayAnalysis() {
    return findTodayAnalysisQuery.execute();
  }

  @Override
  public void save(long analysisId) {
    insertAnalysisQuery.update(analysisId);
  }
}
