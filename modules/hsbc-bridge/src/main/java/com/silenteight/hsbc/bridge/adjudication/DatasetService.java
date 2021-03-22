package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import java.util.List;

@RequiredArgsConstructor
class DatasetService {

  private final DatasetServiceBlockingStub datasetServiceBlockingStub;

  String createDataset(List<String> alertIds) {
    NamedAlerts namedAlerts = NamedAlerts.newBuilder()
        .addAllAlerts(alertIds)
        .build();

    CreateDatasetRequest createDatasetRequest = CreateDatasetRequest.newBuilder()
        .setNamedAlerts(namedAlerts)
        .build();

    Dataset dataset = datasetServiceBlockingStub.createDataset(createDatasetRequest);

    return dataset.getName();
  }

}
