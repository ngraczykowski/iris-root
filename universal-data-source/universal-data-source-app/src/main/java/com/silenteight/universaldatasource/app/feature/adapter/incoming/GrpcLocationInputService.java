package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class GrpcLocationInputService extends LocationInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      StreamObserver<BatchGetMatchLocationInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchLocationInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
