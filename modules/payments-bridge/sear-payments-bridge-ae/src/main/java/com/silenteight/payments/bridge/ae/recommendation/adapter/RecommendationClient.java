package com.silenteight.payments.bridge.ae.recommendation.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class RecommendationClient implements RecommendationClientPort {

  private final RecommendationServiceBlockingStub stub;

  private final Duration timeout;

  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public RecommendationWithMetadata receiveRecommendation(GetRecommendationRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    log.debug(
        "Requesting recommendation: deadline={}, recommendation={}", deadline,
        request.getRecommendation());
    return stub.withDeadline(deadline).getRecommendationWithMetadata(request);
  }
}
