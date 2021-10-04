package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.gender.v1.GenderInputServiceGrpc.GenderInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcGenderInputService extends GenderInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchGenderInputs(
      BatchGetMatchGenderInputsRequest request,
      StreamObserver<BatchGetMatchGenderInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchGenderInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
