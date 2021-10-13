package com.silenteight.payments.bridge.ae.recommendation.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class RecommendationClient implements RecommendationClientPort {

  private final RecommendationServiceBlockingStub stub;

  private final Duration timeout;

  public Recommendation receiveRecommendation(GetRecommendationRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    log.debug(
        "Requesting recommendation: deadline={}, recommendation={}", deadline,
        request.getRecommendation());

    return stub.withDeadline(deadline).getRecommendation(request);
  }
}
