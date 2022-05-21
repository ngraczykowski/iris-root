package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;
import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class DatasetGrpcAdapter implements DatasetServiceClient {

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

    log.info("Create dataset with alerts={}", alerts);

    var response = datasetServiceBlockingStub
        .withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS)
        .createDataset(grpcRequest);

    return response.getName();
  }
}
