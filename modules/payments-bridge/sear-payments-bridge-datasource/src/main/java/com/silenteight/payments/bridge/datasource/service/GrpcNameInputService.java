package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcNameInputService extends NameInputServiceImplBase {

  private final FeatureService featureService;

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {

    featureService.batchGetMatchNameInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
