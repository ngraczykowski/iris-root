package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class AlertServiceGrpc extends AlertServiceImplBase {

  @Override
  public void createAlert(
      CreateAlertRequest request,
      StreamObserver<Alert> responseObserver) {
    responseObserver.onNext(MockAlertUseCase.createAlert(request.getAlert()));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request,
      StreamObserver<BatchCreateAlertMatchesResponse> responseObserver) {
    responseObserver.onNext(MockAlertUseCase.batchCreateAlertMatches(request));
    responseObserver.onCompleted();
  }
}
