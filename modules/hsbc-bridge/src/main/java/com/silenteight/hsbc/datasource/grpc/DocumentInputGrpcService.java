package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentInputServiceGrpc.DocumentInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class DocumentInputGrpcService extends DocumentInputServiceImplBase {

  @Override
  public void batchGetMatchDocumentInputs(
      BatchGetMatchDocumentInputsRequest request,
      StreamObserver<BatchGetMatchDocumentInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchDocumentInputsResponse toResponse() {
    return BatchGetMatchDocumentInputsResponse.newBuilder().build();
  }
}
