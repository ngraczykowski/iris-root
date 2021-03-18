package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInputServiceGrpc.NationalIdInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class NationalIdInputGrpcService extends NationalIdInputServiceImplBase {

  @Override
  public void batchGetMatchNationalIdInputs(
      BatchGetMatchNationalIdInputsRequest request,
      StreamObserver<BatchGetMatchNationalIdInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchNationalIdInputsResponse toResponse() {
    return BatchGetMatchNationalIdInputsResponse.newBuilder().build();
  }
}
