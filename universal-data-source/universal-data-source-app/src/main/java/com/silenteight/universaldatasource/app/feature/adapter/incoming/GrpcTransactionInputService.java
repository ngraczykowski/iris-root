package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.datasource.api.transaction.v1.TransactionInputServiceGrpc.TransactionInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcTransactionInputService extends TransactionInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchTransactionInputs(
      BatchGetMatchTransactionInputsRequest request,
      StreamObserver<BatchGetMatchTransactionInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchTransactionInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
