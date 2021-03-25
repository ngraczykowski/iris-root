package com.silenteight.simulator.dataset.grpc.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.create.CreateDatasetService;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;

@RequiredArgsConstructor
class GrpcCreateDatasetService implements CreateDatasetService {

  @NonNull
  private final DatasetServiceBlockingStub datasetStub;

  @Override
  public Dataset createDataset(CreateDatasetRequest request) {
    return datasetStub.createDataset(toGrpcRequest(request));
  }

  private static com.silenteight.adjudication.api.v1.CreateDatasetRequest toGrpcRequest(
      CreateDatasetRequest request) {

    return com.silenteight.adjudication.api.v1.CreateDatasetRequest.newBuilder()
        .setFilteredAlerts(toFilteredAlerts(request))
        .build();
  }

  private static FilteredAlerts toFilteredAlerts(CreateDatasetRequest request) {
    return FilteredAlerts.newBuilder()
        .setAlertTimeRange(toAlertTimeRange(request))
        .build();
  }

  private static AlertTimeRange toAlertTimeRange(CreateDatasetRequest request) {
    return AlertTimeRange.newBuilder()
        .setStartTime(toTimestamp(request.getRangeFrom()))
        .setEndTime(toTimestamp(request.getRangeTo()))
        .build();
  }
}
