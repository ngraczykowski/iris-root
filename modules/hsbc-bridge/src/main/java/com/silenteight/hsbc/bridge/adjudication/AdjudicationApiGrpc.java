package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

@RequiredArgsConstructor
class AdjudicationApiGrpc implements AdjudicationApi {

  private final AlertServiceBlockingStub alertServiceBlockingStub;
  private final DatasetServiceBlockingStub datasetServiceBlockingStub;

  @Override
  public BatchCreateAlertsResponse batchCreateAlerts(
      BatchCreateAlertsRequest request) {
    return alertServiceBlockingStub.batchCreateAlerts(request);
  }

  @Override
  public BatchCreateAlertMatchesResponse batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request) {
    return alertServiceBlockingStub.batchCreateAlertMatches(request);
  }

  @Override
  public Dataset createDataset(CreateDatasetRequest request) {
    return datasetServiceBlockingStub.createDataset(request);
  }
}
