package com.silenteight.adjudication.engine.dataset.dataset;

import java.time.OffsetDateTime;
import java.util.List;

public interface DatasetAlertDataAccess {

  List<Long> selectDatasetsByAlerts(List<Long> alertIds);

  void createSingleMatchFilteredDataset(
      long datasetId,
      List<String> labelsValues,
      OffsetDateTime startDate,
      OffsetDateTime endDate);
}
