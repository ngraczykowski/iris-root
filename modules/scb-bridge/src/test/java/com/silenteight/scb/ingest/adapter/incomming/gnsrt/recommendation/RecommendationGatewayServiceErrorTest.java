package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsRequest;
import com.silenteight.proto.serp.v1.api.GenerateRecommendationsResponse;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.RecommendationGatewayImplBase;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;
import javax.annotation.Nonnull;

import static java.util.List.of;

class RecommendationGatewayServiceErrorTest
    extends AbstractRecommendationGatewayServiceTest {

  @Test
  public void shouldCallForRecommendationAndHandleRuntimeException() {
    //given
    Alert alert = createAlert("systemId");

    //when
    Flux<GenerateRecommendationsResponse> result = underTest.recommend(of(alert));

    //then
    StepVerifier
        .create(result)
        .verifyErrorMatches(getExceptionPredicate());
  }

  @Nonnull
  private static Predicate<Throwable> getExceptionPredicate() {
    return t -> t instanceof StatusRuntimeException
        && ((StatusRuntimeException) t).getStatus().getCode() == Status.Code.INTERNAL;
  }

  @Override
  RecommendationGatewayImplBase getMockedService() {
    return new RecommendationGatewayImplBase() {

      @Override
      public Flux<GenerateRecommendationsResponse> generateRecommendations(
          Mono<GenerateRecommendationsRequest> request) {
        throw Status.INTERNAL.asRuntimeException();
      }
    };
  }
}
