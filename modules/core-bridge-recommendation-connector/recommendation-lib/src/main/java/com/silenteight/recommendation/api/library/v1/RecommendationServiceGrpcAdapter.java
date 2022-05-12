package com.silenteight.recommendation.api.library.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.recommendation.api.v1.RecommendationResponse;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceBlockingStub;

import io.vavr.control.Try;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RecommendationServiceGrpcAdapter implements RecommendationServiceClient {

  private static final String COULD_NOT_GET_RECOMMENDATIONS = "Couldn't get recommendations";

  private final RecommendationServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public RecommendationsOut getRecommendations(RecommendationsIn request) {
    return Try.of(() -> getStub().getRecommendations(request.toRecommendationsRequest()))
        .map(RecommendationsOut::createFrom)
        .onFailure(e -> log.error(COULD_NOT_GET_RECOMMENDATIONS, e))
        .onSuccess(emptyOut -> log.debug("Successfully got recommendations"))
        .getOrElseThrow(e -> new RecommendationLibraryException(COULD_NOT_GET_RECOMMENDATIONS, e));
  }

  @Override
  public Iterator<RecommendationResponse> streamRecommendations(RecommendationsIn request) {
    return getStub().streamRecommendations(request.toRecommendationsRequest());
  }

  private RecommendationServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
