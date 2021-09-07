package com.silenteight.payments.bridge.datasource.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextInputServiceGrpc.FreeTextInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcFreeTextInputService extends FreeTextInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchFreeTextInputs(
      BatchGetMatchFreeTextInputsRequest request,
      StreamObserver<BatchGetMatchFreeTextInputsResponse> responseObserver) {

  }
}
