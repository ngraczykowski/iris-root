package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.dataset.dataset.DatasetAlertDataAccess;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcDatasetAlertDataAccess implements DatasetAlertDataAccess {

  private final SelectDatasetsByAlertsQuery selectDatasetsByAlertsQuery;
  private final CreateSingleMatchFilteredDatasetQuery createSingleMatchFilteredDatasetQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<Long> selectDatasetsByAlerts(List<Long> alertIds) {
    return selectDatasetsByAlertsQuery.execute(alertIds);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void createSingleMatchFilteredDataset(long datasetId, List<String> labelsValues,
      OffsetDateTime startDate, OffsetDateTime endDate) {
    createSingleMatchFilteredDatasetQuery.execute(datasetId, labelsValues, startDate, endDate);
  }
}
