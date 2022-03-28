package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.dataset.dataset.DatasetAlertDataAccess;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcDatasetAlertDataAccess implements DatasetAlertDataAccess {

  private final SelectDatasetsByAlertsQuery selectDatasetsByAlertsQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public List<Long> selectDatasetsByAlerts(List<Long> alertIds) {
    return selectDatasetsByAlertsQuery.execute(alertIds);
  }
}
