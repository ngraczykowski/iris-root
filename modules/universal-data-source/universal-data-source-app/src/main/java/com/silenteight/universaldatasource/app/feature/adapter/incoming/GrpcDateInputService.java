package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsRequest;
import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;
import com.silenteight.datasource.api.date.v1.DateInputServiceGrpc.DateInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcDateInputService extends DateInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchDateInputs(
      BatchGetMatchDateInputsRequest request,
      StreamObserver<BatchGetMatchDateInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchDateInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
