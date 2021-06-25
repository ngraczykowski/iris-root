package com.silenteight.adjudication.engine.alerts.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcAlertServiceImpl extends AlertServiceImplBase {

  @NonNull
  private final AlertService alertService;

  @Override
  public void createAlert(
      CreateAlertRequest request, StreamObserver<Alert> responseObserver) {
    responseObserver.onNext(alertService.createAlert(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlerts(
      BatchCreateAlertsRequest request,
      StreamObserver<BatchCreateAlertsResponse> responseObserver) {
    responseObserver.onNext(alertService.batchCreateAlerts(request));
    responseObserver.onCompleted();
  }

  @Override
  public void createMatch(CreateMatchRequest request, StreamObserver<Match> responseObserver) {
    responseObserver.onNext(alertService.createMatch(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request,
      StreamObserver<BatchCreateAlertMatchesResponse> responseObserver) {
    responseObserver.onNext(alertService.batchCreateAlertMatches(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateMatches(
      BatchCreateMatchesRequest request,
      StreamObserver<BatchCreateMatchesResponse> responseObserver) {
    responseObserver.onNext(alertService.batchCreateMatches(request));
    responseObserver.onCompleted();
  }
}
