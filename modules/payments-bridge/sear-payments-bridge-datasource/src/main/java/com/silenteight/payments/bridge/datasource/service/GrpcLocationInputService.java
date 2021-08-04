package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcLocationInputService extends LocationInputServiceImplBase {

  private final FeatureService featureService;

  @Override
  public void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      StreamObserver<BatchGetMatchLocationInputsResponse> responseObserver) {

    featureService.batchGetMatchLocationInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
