package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentInputServiceGrpc.DocumentInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcDocumentInputService extends DocumentInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchDocumentInputs(
      BatchGetMatchDocumentInputsRequest request,
      StreamObserver<BatchGetMatchDocumentInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchDocumentInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
