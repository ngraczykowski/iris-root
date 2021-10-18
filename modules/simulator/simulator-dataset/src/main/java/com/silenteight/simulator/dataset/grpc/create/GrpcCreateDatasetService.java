package com.silenteight.simulator.dataset.grpc.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelValues;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelsFilter;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.create.CreateDatasetService;
import com.silenteight.simulator.dataset.create.DatasetLabel;

import java.util.List;
import java.util.Map;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
class GrpcCreateDatasetService implements CreateDatasetService {

  @NonNull
  private final DatasetServiceBlockingStub datasetStub;

  @Override
  public Dataset createDataset(CreateDatasetRequest request) {
    log.debug("Creating dataset with CreateDatasetRequest={}", request);

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
        .setLabelsFilter(toLabelsFilter(request.getLabels()))
        .build();
  }

  private static AlertTimeRange toAlertTimeRange(CreateDatasetRequest request) {
    return AlertTimeRange.newBuilder()
        .setStartTime(toTimestamp(request.getRangeFrom()))
        .setEndTime(toTimestamp(request.getRangeTo()))
        .build();
  }

  private static LabelsFilter toLabelsFilter(List<DatasetLabel> labels) {
    return LabelsFilter.newBuilder()
        .putAllLabels(toLabelsMap(labels))
        .build();
  }

  private static Map<String, LabelValues> toLabelsMap(List<DatasetLabel> labels) {
    return labels
        .stream()
        .collect(toMap(DatasetLabel::getName, label -> toLabelValues(label.getValues())));
  }

  private static LabelValues toLabelValues(List<String> values) {
    return LabelValues.newBuilder()
        .addAllValue(values)
        .build();
  }
}
