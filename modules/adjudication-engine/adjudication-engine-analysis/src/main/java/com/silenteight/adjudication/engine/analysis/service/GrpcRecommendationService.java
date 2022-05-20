package com.silenteight.adjudication.engine.analysis.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.*;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcRecommendationService extends RecommendationServiceImplBase {

  private final RecommendationService recommendationService;

  @Override
  public void streamRecommendations(
      StreamRecommendationsRequest request,
      StreamObserver<Recommendation> responseObserver) {

    recommendationService.streamRecommendations(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }

  @Override
  public void streamRecommendationsWithMetadata(
      StreamRecommendationsWithMetadataRequest request,
      StreamObserver<RecommendationWithMetadata> responseObserver) {

    recommendationService.streamRecommendationsWithMetadata(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendation(
      GetRecommendationRequest request,
      StreamObserver<Recommendation> responseObserver) {

    responseObserver.onNext(recommendationService.getRecommendation(request));
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendationMetadata(
      GetRecommendationMetadataRequest request,
      StreamObserver<RecommendationMetadata> responseObserver) {

    responseObserver.onNext(recommendationService.getRecommendationMetadata(request));
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendationWithMetadata(
      GetRecommendationRequest request,
      StreamObserver<RecommendationWithMetadata> responseObserver) {

    responseObserver.onNext(recommendationService.getRecommendationWithMetadata(request));
    responseObserver.onCompleted();
  }
}
