package com.silenteight.adjudication.engine.dataset.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest.FilterCase;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceImplBase;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import javax.validation.Valid;

@GrpcService
@Slf4j
@Profile("mock")
class MockedGrpcDatasetServiceImpl extends DatasetServiceImplBase {

  private final Random random = new Random();

  private String generateRandomPageToken() {
    byte[] randomBytes = new byte[16];
    random.nextBytes(randomBytes);
    return Base64.getEncoder().encodeToString(randomBytes);
  }

  @Override
  public void createDataset(
      CreateDatasetRequest request, StreamObserver<Dataset> responseObserver) {
    log.info("Create dataset MOCKED GRPC call");
    if (request.getFilterCase() == FilterCase.FILTER_NOT_SET) {
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("filter must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(doCreateDataset(request));
    responseObserver.onCompleted();
  }

  private Dataset doCreateDataset(@Valid CreateDatasetRequest request) {
    int alertsCount;

    if (request.hasNamedAlerts())
      alertsCount = request.getNamedAlerts().getAlertsCount();
    else
      alertsCount = 123;

    return Dataset
        .newBuilder()
        .setName("datasets/" + UUID.randomUUID())
        .setAlertCount(alertsCount)
        .setCreateTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
        .build();
  }

  @Override
  public void getDataset(
      GetDatasetRequest request, StreamObserver<Dataset> responseObserver) {
    responseObserver.onNext(doGetDataset(request));
    responseObserver.onCompleted();
  }

  @NotNull
  private Dataset doGetDataset(@Valid GetDatasetRequest request) {
    return Dataset
        .newBuilder()
        .setName(request.getName())
        .setAlertCount(123)
        .setCreateTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
        .build();
  }

  @Override
  public void listDatasets(
      ListDatasetsRequest request, StreamObserver<ListDatasetsResponse> responseObserver) {
    if (request.getPageSize() <= 0) {
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("pageSize must be greater than 0")
          .asRuntimeException());
      return;
    }

    ListDatasetsResponse.Builder builder = ListDatasetsResponse.newBuilder();
    for (int i = 0; i < request.getPageSize(); i++) {
      Dataset dataset = Dataset
          .newBuilder()
          .setName("datasets/" + UUID.randomUUID())
          .setAlertCount(123)
          .setCreateTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
          .build();
      builder.addDatasets(dataset);
    }
    builder.setNextPageToken(generateRandomPageToken());
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void listDatasetAlerts(
      ListDatasetAlertsRequest request,
      StreamObserver<ListDatasetAlertsResponse> responseObserver) {
    if (request.getPageSize() <= 0) {
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("pageSize must be greater than 0")
          .asRuntimeException());
      return;
    }

    if (request.getDataset().isBlank()) {
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("dataset must be set")
          .asRuntimeException());
      return;
    }

    ListDatasetAlertsResponse.Builder builder = ListDatasetAlertsResponse.newBuilder();
    for (int i = 0; i < request.getPageSize(); i++) {
      builder.addDatasetAlertNames(
          request.getDataset() + "/alerts/" + UUID.randomUUID().toString());
    }
    builder.setNextPageToken(generateRandomPageToken());
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
