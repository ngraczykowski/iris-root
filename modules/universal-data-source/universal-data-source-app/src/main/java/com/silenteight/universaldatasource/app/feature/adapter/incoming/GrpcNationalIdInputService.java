package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInputServiceGrpc.NationalIdInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcNationalIdInputService extends NationalIdInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchNationalIdInputs(
      BatchGetMatchNationalIdInputsRequest request,
      StreamObserver<BatchGetMatchNationalIdInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchNationalIdInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
