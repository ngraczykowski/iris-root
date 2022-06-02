package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.compareDates.v1.BatchGetCompareDatesInputsRequest;
import com.silenteight.datasource.api.compareDates.v1.BatchGetCompareDatesInputsResponse;
import com.silenteight.datasource.api.compareDates.v1.CompareDatesInputServiceGrpc.CompareDatesInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcCompareDatesInputService extends CompareDatesInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetCompareDatesInputs(
      BatchGetCompareDatesInputsRequest request,
      StreamObserver<BatchGetCompareDatesInputsResponse> responseObserver) {

    featureAdapter.batchGetCompareDatesInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
