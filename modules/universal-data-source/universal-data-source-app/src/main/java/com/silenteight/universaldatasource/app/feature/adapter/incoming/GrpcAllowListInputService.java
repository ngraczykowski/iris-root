package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.allowlist.v1.AllowListInputServiceGrpc.AllowListInputServiceImplBase;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsRequest;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcAllowListInputService extends AllowListInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchAllowListInputs(
      BatchGetMatchAllowListInputsRequest request,
      StreamObserver<BatchGetMatchAllowListInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchAllowListInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
