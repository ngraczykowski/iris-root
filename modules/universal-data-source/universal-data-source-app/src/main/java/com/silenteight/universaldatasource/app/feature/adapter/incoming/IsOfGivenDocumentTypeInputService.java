package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.BatchGetIsOfGivenDocumentTypeInputsRequest;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.BatchGetIsOfGivenDocumentTypeInputsResponse;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeInputServiceGrpc.IsOfGivenDocumentTypeInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class IsOfGivenDocumentTypeInputService extends IsOfGivenDocumentTypeInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetIsOfGivenDocumentTypeInputs(
      BatchGetIsOfGivenDocumentTypeInputsRequest request,
      StreamObserver<BatchGetIsOfGivenDocumentTypeInputsResponse> responseObserver) {

    featureAdapter.batchGetIsOfGivenDocumentTypeInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }


}
