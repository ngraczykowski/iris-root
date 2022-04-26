package com.silenteight.adjudication.engine.dataset.dataset;

import java.time.OffsetDateTime;
import java.util.List;

class InMemoryDatasetAlertDataAccess implements DatasetAlertDataAccess {

  @Override
  public List<Long> selectDatasetsByAlerts(List<Long> alertIds) {
    return null;
  }

  @Override
  public void createSingleMatchFilteredDataset(
      long datasetId, List<String> labelsValues, OffsetDateTime startDate, OffsetDateTime endDate) {

  }
}
