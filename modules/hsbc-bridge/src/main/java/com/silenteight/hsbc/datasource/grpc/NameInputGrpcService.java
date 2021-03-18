package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class NameInputGrpcService extends NameInputServiceImplBase {

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchNameInputsResponse toResponse() {
    return BatchGetMatchNameInputsResponse.newBuilder().build();
  }
}
