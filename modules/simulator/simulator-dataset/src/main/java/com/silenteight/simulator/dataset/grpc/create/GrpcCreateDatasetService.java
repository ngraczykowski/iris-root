package com.silenteight.simulator.dataset.grpc.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelValues;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelsFilter;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.create.CreateDatasetService;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.Map.of;

@RequiredArgsConstructor
class GrpcCreateDatasetService implements CreateDatasetService {

  private static final String COUNTRY_LABEL = "country";

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
        .setLabelsFilter(toLabelsFilter(COUNTRY_LABEL, request.getCountries()))
        .build();
  }

  private static AlertTimeRange toAlertTimeRange(CreateDatasetRequest request) {
    return AlertTimeRange.newBuilder()
        .setStartTime(toTimestamp(request.getRangeFrom()))
        .setEndTime(toTimestamp(request.getRangeTo()))
        .build();
  }

  private static LabelsFilter toLabelsFilter(String label, List<String> values) {
    return LabelsFilter.newBuilder()
        .putAllLabels(of(label, toLabelValues(values)))
        .build();
  }

  private static LabelValues toLabelValues(List<String> values) {
    return LabelValues.newBuilder()
        .addAllValue(values)
        .build();
  }
}
