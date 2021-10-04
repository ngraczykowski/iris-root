package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsRequest;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse;
import com.silenteight.datasource.api.ispep.v1.IsPepInputServiceGrpc.IsPepInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcIsPepInputService extends IsPepInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchIsPepSolutions(
      BatchGetMatchIsPepSolutionsRequest request,
      StreamObserver<BatchGetMatchIsPepSolutionsResponse> responseObserver) {

    featureAdapter.batchGetMatchIsPepSolutions(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
