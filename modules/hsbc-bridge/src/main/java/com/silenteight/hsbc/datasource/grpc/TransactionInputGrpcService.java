package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.datasource.api.transaction.v1.TransactionInputServiceGrpc.TransactionInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class TransactionInputGrpcService extends TransactionInputServiceImplBase {

  @Override
  public void batchGetMatchTransactionInputs(
      BatchGetMatchTransactionInputsRequest request,
      StreamObserver<BatchGetMatchTransactionInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchTransactionInputsResponse toResponse() {
    return BatchGetMatchTransactionInputsResponse.newBuilder().build();
  }
}
