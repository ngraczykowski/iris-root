package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
class DatasetServiceApiGrpc implements DatasetServiceApi {

  private final DatasetServiceBlockingStub datasetServiceBlockingStub;

  @Override
  public DatasetDto createDataset(Collection<String> alerts) {
    var grpcRequest = CreateDatasetRequest.newBuilder()
        .setNamedAlerts(NamedAlerts.newBuilder()
            .addAllAlerts(alerts)
            .build())
        .build();

    var response = datasetServiceBlockingStub.createDataset(grpcRequest);

    return new DatasetDto(response.getName());
  }
}
