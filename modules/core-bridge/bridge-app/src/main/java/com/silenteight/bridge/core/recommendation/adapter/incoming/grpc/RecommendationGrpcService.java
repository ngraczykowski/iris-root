package com.silenteight.bridge.core.recommendation.adapter.incoming.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc;
import com.silenteight.proto.recommendation.api.v1.RecommendationsRequest;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class RecommendationGrpcService extends RecommendationServiceGrpc.RecommendationServiceImplBase {

  private final RecommendationFacade recommendationFacade;

  @Override
  public void getRecommendations(
      RecommendationsRequest request, StreamObserver<RecommendationsResponse> responseObserver) {
    var command =
        new GetRecommendationCommand(request.getAnalysisName(), request.getAlertNamesList());

    log.info("Get recommendation response for analysis name [{}].", command.analysisName());
    responseObserver.onNext(recommendationFacade.getRecommendationsResponse(command));
    responseObserver.onCompleted();
  }
}
