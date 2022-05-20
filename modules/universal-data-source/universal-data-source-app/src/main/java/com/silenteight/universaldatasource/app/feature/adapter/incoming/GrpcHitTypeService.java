package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.hittype.v1.BatchGetMatchHitTypeInputsRequest;
import com.silenteight.datasource.api.hittype.v1.BatchGetMatchHitTypeInputsResponse;
import com.silenteight.datasource.api.hittype.v1.HitTypeInputServiceGrpc.HitTypeInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
class GrpcHitTypeService extends HitTypeInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchHitTypeInputs(
      BatchGetMatchHitTypeInputsRequest request,
      StreamObserver<BatchGetMatchHitTypeInputsResponse> responseObserver) {
    featureAdapter.batchGetMatchHitTypeInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
