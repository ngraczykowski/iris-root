package com.silenteight.adjudication.engine.dataset.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
@RequiredArgsConstructor
class GrpcDatasetServiceImpl extends DatasetServiceImplBase {

  @NonNull
  private final DatasetService datasetService;

  @Override
  public void createDataset(
      CreateDatasetRequest request, StreamObserver<Dataset> responseObserver) {

    responseObserver.onNext(datasetService.createDataset(request));
    responseObserver.onCompleted();
  }

  @Override
  public void getDataset(
      GetDatasetRequest request, StreamObserver<Dataset> responseObserver) {

    responseObserver.onNext(datasetService.getDataset(request));
    responseObserver.onCompleted();
  }

  @Override
  public void listDatasets(
      ListDatasetsRequest request, StreamObserver<ListDatasetsResponse> responseObserver) {

    responseObserver.onNext(datasetService.listDataset(request));
    responseObserver.onCompleted();
  }

  @Override
  public void listDatasetAlerts(
      ListDatasetAlertsRequest request,
      StreamObserver<ListDatasetAlertsResponse> responseObserver) {

    responseObserver.onNext(datasetService.listDatasetAlerts(request));
    responseObserver.onCompleted();
  }
}
