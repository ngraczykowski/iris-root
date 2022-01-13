package com.silenteight.adjudication.api.library.v1.dataset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.NamedAlerts;

import io.vavr.control.Try;

import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
public class DatasetGrpcAdapter implements DatasetServiceClient {

  private static final String CANNOT_CREATE_DATASET = "Cannot create dataset";
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

    return Try.of(() -> getStub().createDataset(grpcRequest).getName())
        .onFailure(e -> log.error(CANNOT_CREATE_DATASET, e))
        .onSuccess(result -> log.debug("Dataset was created successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_CREATE_DATASET, e));
  }

  private DatasetServiceBlockingStub getStub() {
    return datasetServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
