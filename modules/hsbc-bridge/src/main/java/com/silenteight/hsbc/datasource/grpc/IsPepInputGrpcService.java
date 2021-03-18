package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v1.IsPepInputServiceGrpc.IsPepInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class IsPepInputGrpcService extends IsPepInputServiceImplBase {

  @Override
  public void batchGetMatchIsPepInputs(
      BatchGetMatchIsPepInputsRequest request,
      StreamObserver<BatchGetMatchIsPepInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchIsPepInputsResponse toResponse() {
    return BatchGetMatchIsPepInputsResponse.newBuilder().build();
  }
}
