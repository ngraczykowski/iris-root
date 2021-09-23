package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchAddAlertsResponse;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class AnalysisServiceGrpc extends AnalysisServiceImplBase {

  @Override
  public void createAnalysis(
      CreateAnalysisRequest request,
      StreamObserver<Analysis> responseObserver) {
    responseObserver.onNext(Analysis.newBuilder().setName("analysis/420").build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchAddAlerts(
      BatchAddAlertsRequest request,
      StreamObserver<BatchAddAlertsResponse> responseObserver) {
    responseObserver.onNext(BatchAddAlertsResponse.newBuilder().addAllAnalysisAlerts(
        request.getAnalysisAlertsList()).build());
    responseObserver.onCompleted();
  }
}
