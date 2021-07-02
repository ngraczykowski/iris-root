package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.Collection;

import static java.util.concurrent.TimeUnit.*;

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

    log.info("NOMAD, create dataset with alerts={}", alerts);

    var response = datasetServiceBlockingStub
        .withDeadlineAfter(deadlineInSeconds, SECONDS)
        .createDataset(grpcRequest);

    return response.getName();
  }
}
