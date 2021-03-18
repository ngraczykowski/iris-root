package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.gender.v1.GenderInputServiceGrpc.GenderInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class GenderInputGrpcService extends GenderInputServiceImplBase {

  @Override
  public void batchGetMatchGenderInputs(
      BatchGetMatchGenderInputsRequest request,
      StreamObserver<BatchGetMatchGenderInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchGenderInputsResponse toResponse() {
    return BatchGetMatchGenderInputsResponse.newBuilder().build();
  }
}
