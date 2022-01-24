package com.silenteight.bridge.core.recommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc;
import com.silenteight.proto.recommendation.api.v1.RecommendationsRequest;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class RecommendationGrpcService extends RecommendationServiceGrpc.RecommendationServiceImplBase {

  private final RecommendationFacade recommendationFacade;
  private final RegistrationFacade registrationFacade;

  @Override
  public void getRecommendations(
      RecommendationsRequest request, StreamObserver<RecommendationsResponse> responseObserver) {
    var recommendations = getRecommendationWithMetadata(request);
    var batchWithAlerts = getBatchWithAlerts(request);
    var recommendedAtForErrorAlerts = OffsetDateTime.now();

    responseObserver.onNext(
        RecommendationMapper.toRecommendationsResponse(
            batchWithAlerts, recommendations, recommendedAtForErrorAlerts));
    responseObserver.onCompleted();
  }

  private List<RecommendationWithMetadata> getRecommendationWithMetadata(
      RecommendationsRequest request) {
    var command = new GetRecommendationsCommand(request.getAnalysisId());
    return recommendationFacade.getRecommendations(command);
  }

  private BatchWithAlerts getBatchWithAlerts(RecommendationsRequest request) {
    var command = new GetBatchWithAlertsCommand(request.getAnalysisId());
    return registrationFacade.getBatchWithAlerts(command);
  }
}
