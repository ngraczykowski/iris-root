package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceImplBase;

import io.grpc.stub.StreamObserver;

import lombok.extern.slf4j.Slf4j;

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

    if (log.isDebugEnabled()) {
      var matches = request.getMatchesList();

      log.debug(
          "Streaming feature inputs: agentInputType={}, features={}, matchCount={}"
              + ", firstTenMatches={}",
          "GEO", request.getFeaturesList(),
          matches.size(), matches.subList(0, Math.min(10, matches.size())));
    }

    featureAdapter.batchGetMatchLocationInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
