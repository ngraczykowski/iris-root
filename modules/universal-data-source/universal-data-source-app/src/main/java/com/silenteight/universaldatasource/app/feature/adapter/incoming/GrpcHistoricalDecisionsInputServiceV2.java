package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcHistoricalDecisionsInputServiceV2 extends HistoricalDecisionsInputServiceImplBase {

  private final FeatureAdapterV2 featureAdapter;

  @Override
  public void batchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsRequest request,
      StreamObserver<BatchGetMatchHistoricalDecisionsInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchHistoricalDecisionsInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
