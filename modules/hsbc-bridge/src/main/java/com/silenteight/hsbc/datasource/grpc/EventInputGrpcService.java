package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.event.v1.EventInputServiceGrpc.EventInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
class EventInputGrpcService extends EventInputServiceImplBase {

  @Override
  public void batchGetMatchEventInputs(
      BatchGetMatchEventInputsRequest request,
      StreamObserver<BatchGetMatchEventInputsResponse> responseObserver) {
    responseObserver.onNext(toResponse());
    responseObserver.onCompleted();
  }

  private BatchGetMatchEventInputsResponse toResponse() {
    return BatchGetMatchEventInputsResponse.newBuilder().build();
  }
}
