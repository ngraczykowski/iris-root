package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.dataset.dataset.DatasetAlertDataAccess;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcDatasetAlertDataAccess implements DatasetAlertDataAccess {

  private final SelectDatasetsByAlertsQuery selectDatasetsByAlertsQuery;

  @Override
  public List<Long> selectDatasetsByAlerts(List<Long> alertIds) {
    return selectDatasetsByAlertsQuery.execute(alertIds);
  }
}
