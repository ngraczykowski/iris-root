package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

@RequiredArgsConstructor
class DatasetServiceApiGrpc implements DatasetServiceApi {

  private final DatasetServiceBlockingStub datasetServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public String createDataset(Collection<String> alerts) {
    var grpcRequest = CreateDatasetRequest.newBuilder()
        .setNamedAlerts(NamedAlerts.newBuilder()
            .addAllAlerts(alerts)
            .build())
        .build();

    var response = datasetServiceBlockingStub
        .withDeadlineAfter(deadlineInSeconds, SECONDS)
        .createDataset(grpcRequest);

    return response.getName();
  }
}
