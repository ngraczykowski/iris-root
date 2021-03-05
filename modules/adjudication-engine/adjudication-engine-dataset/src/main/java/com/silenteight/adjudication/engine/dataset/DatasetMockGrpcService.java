package com.silenteight.adjudication.engine.dataset;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest.FilterCase;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceImplBase;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

// TODO tkleszcz: mocked service replace with real one
@GrpcService
class DatasetMockGrpcService extends DatasetServiceImplBase {

  private final Random random = new Random();

  private String generateRandomPageToken() {
    byte[] randomBytes = new byte[16];
    random.nextBytes(randomBytes);
    return Base64.getEncoder().encodeToString(randomBytes);
  }

  @Override
  public void createDataset(
      CreateDatasetRequest request, StreamObserver<Dataset> responseObserver) {
    if (request.getFilterCase() == FilterCase.FILTER_NOT_SET)
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("filter must be set")
          .asRuntimeException());

    int alertsCount;

    if (request.hasAlertNames())
      alertsCount = request.getAlertNames().getAlertNamesCount();
    else
      alertsCount = 123;

    Dataset dataset = Dataset
        .newBuilder()
        .setName("datasets/" + UUID.randomUUID())
        .setAlertCount(alertsCount)
        .setCreateTime(Timestamp.newBuilder().setSeconds(System.currentTimeMillis()))
        .build();
    responseObserver.onNext(dataset);
    responseObserver.onCompleted();
  }

  @Override
  public void getDataset(
      GetDatasetRequest request, StreamObserver<Dataset> responseObserver) {
    if (request.getName().isBlank())
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("name must be set")
          .asRuntimeException());

    Dataset dataset = Dataset
        .newBuilder()
        .setName(request.getName())
        .setAlertCount(123)
        .setCreateTime(Timestamp.newBuilder().setSeconds(System.currentTimeMillis()))
        .build();
    responseObserver.onNext(dataset);
    responseObserver.onCompleted();
  }

  @Override
  public void listDatasets(
      ListDatasetsRequest request, StreamObserver<ListDatasetsResponse> responseObserver) {
    if (request.getPageSize() <= 0)
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("pageSize must be greater than 0")
          .asRuntimeException());

    ListDatasetsResponse.Builder builder = ListDatasetsResponse.newBuilder();
    for (int i = 0; i < request.getPageSize(); i++) {
      builder.addDatasets("datasets/" + UUID.randomUUID().toString());
    }
    builder.setNextPageToken(generateRandomPageToken());
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void listDatasetAlerts(
      ListDatasetAlertsRequest request,
      StreamObserver<ListDatasetAlertsResponse> responseObserver) {
    if (request.getPageSize() <= 0)
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("pageSize must be greater than 0")
          .asRuntimeException());

    if (request.getDataset().isBlank())
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("dataset must be set")
          .asRuntimeException());

    ListDatasetAlertsResponse.Builder builder = ListDatasetAlertsResponse.newBuilder();
    for (int i = 0; i < request.getPageSize(); i++) {
      builder.addDatasetAlertNames(
          "datasets/" + request.getDataset() + "/alerts/" + UUID.randomUUID().toString());
    }
    builder.setNextPageToken(generateRandomPageToken());
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
