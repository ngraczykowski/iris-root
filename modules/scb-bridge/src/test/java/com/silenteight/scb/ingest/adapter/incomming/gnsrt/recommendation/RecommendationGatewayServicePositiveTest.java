package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsRequest;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsResponse;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.RecommendationGatewayImplBase;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class RecommendationGatewayServicePositiveTest
    extends AbstractRecommendationGatewayServiceTest {

  @Test
  public void shouldCallReactiveGrpcClientForRecommendation() {
    //given
    Alert alert1 = createAlert("systemId1");
    Alert alert2 = createAlert("systemId2");

    //when
    Flux<GenerateRecommendationsResponse> result = underTest.recommend(of(alert1, alert2));

    //then
    StepVerifier
        .create(result)
        .assertNext(response -> assertResponseAlertId(response, alert1))
        .assertNext(response -> assertResponseAlertId(response, alert2))
        .verifyComplete();
  }

  private static void assertResponseAlertId(GenerateRecommendationsResponse response, Alert alert) {
    assertThat(response.getAlertRecommendation().getAlertId()).isEqualTo(alert.getId());
  }

  @Override
  RecommendationGatewayImplBase getMockedService() {
    return new RecommendationGatewayImplBase() {

      @Override
      public Flux<GenerateRecommendationsResponse> generateRecommendations(
          Mono<GenerateRecommendationsRequest> request) {

        return request.flux()
            .flatMap(r -> Flux.fromIterable(r.getAlertsList()))
            .map(this::alertRecommendation)
            .map(this::response);
      }

      private GenerateRecommendationsResponse response(AlertRecommendation recommendation) {
        return GenerateRecommendationsResponse.newBuilder()
            .setAlertRecommendation(recommendation)
            .build();
      }

      private AlertRecommendation alertRecommendation(Alert alert) {
        return AlertRecommendation.newBuilder()
            .setAlertId(alert.getId())
            .build();
      }
    };
  }
}
