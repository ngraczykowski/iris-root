package com.silenteight.hsbc.bridge.adjudication;

public interface AdjudicationApi {

  com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse batchCreateAlerts(
      com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest request);

  com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse batchCreateAlertMatches(
      com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest request);

  com.silenteight.adjudication.api.v1.Dataset createDataset(
      com.silenteight.adjudication.api.v1.CreateDatasetRequest request);
}
