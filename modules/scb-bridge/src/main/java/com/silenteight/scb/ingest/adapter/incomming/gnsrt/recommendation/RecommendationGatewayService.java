package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsRequest;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsRequest.Builder;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsResponse;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.ReactorRecommendationGatewayStub;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ALERT_ORDER_PRIORITY;

@lombok.Builder
class RecommendationGatewayService {

  private final ReactorRecommendationGatewayStub recommendationGatewayStub;
  private final int deadlineInSeconds;

  public Flux<GenerateRecommendationsResponse> recommend(List<Alert> alerts) {
    GenerateRecommendationsRequest grpcRequest = createGrpcRequest(alerts);

    return recommendationGatewayStub
        .withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS)
        .generateRecommendations(grpcRequest);
  }

  private static GenerateRecommendationsRequest createGrpcRequest(List<Alert> alerts) {
    Builder builder = GenerateRecommendationsRequest.newBuilder().setPriority(ALERT_ORDER_PRIORITY);
    builder.addAllAlerts(alerts);
    return builder.build();
  }
}
