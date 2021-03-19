package com.silenteight.adjudication.engine.alerts.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@RequiredArgsConstructor
@Profile("!mock")
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
}
