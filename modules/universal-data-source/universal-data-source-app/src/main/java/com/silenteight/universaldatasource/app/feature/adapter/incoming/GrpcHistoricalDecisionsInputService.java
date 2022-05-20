package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcHistoricalDecisionsInputService extends HistoricalDecisionsInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsRequest request,
      StreamObserver<BatchGetMatchHistoricalDecisionsInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchHistoricalDecisionsInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
