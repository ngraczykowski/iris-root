package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcIsPepInputService extends IsPepInputServiceImplBase {

  private final FeatureAdapterV2 featureAdapter;

  @Override
  public void batchGetMatchIsPepInputs(
      BatchGetMatchIsPepInputsRequest request,
      StreamObserver<BatchGetMatchIsPepInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchIsPepInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
