package com.silenteight.adjudication.api.library.v1.dataset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import io.grpc.StatusRuntimeException;

import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
public class DatasetGrpcAdapter implements DatasetServiceClient {

  private final DatasetServiceBlockingStub datasetServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public String createDataset(Collection<String> alerts) {
    var grpcRequest = CreateDatasetRequest.newBuilder()
        .setNamedAlerts(NamedAlerts.newBuilder()
            .addAllAlerts(alerts)
            .build())
        .build();

    log.info("Create dataset with alerts={}", alerts);

    try {
      return getStub().createDataset(grpcRequest).getName();
    } catch (StatusRuntimeException e) {
      log.error("Cannot create dataset", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot create dataset", e);
    }
  }

  private DatasetServiceBlockingStub getStub() {
    return datasetServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
