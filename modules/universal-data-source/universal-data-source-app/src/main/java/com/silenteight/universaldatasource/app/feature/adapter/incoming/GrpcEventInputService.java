package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.event.v1.EventInputServiceGrpc.EventInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcEventInputService extends EventInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchEventInputs(
      BatchGetMatchEventInputsRequest request,
      StreamObserver<BatchGetMatchEventInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchEventInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
