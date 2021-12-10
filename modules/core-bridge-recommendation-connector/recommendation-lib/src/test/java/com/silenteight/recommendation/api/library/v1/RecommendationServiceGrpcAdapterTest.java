package com.silenteight.recommendation.api.library.v1;

import com.silenteight.proto.recommendation.api.v1.Recommendation;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceImplBase;
import com.silenteight.proto.recommendation.api.v1.RecommendationsRequest;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class RecommendationServiceGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private RecommendationServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedRecommendationServiceGrpcServer());

    var stub = RecommendationServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new RecommendationServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void getRecommendations() {
    //when
    var response = underTest.getRecommendations(Fixtures.RECOMMENDATIONS_IN);

    //then
    Assertions.assertEquals(response.getRecommendations().size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedRecommendationServiceGrpcServer extends RecommendationServiceImplBase {

    @Override
    public void getRecommendations(
        RecommendationsRequest request, StreamObserver<RecommendationsResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    static final String SOME_ANALYSIS = "some_analysis";
    static final List<String> ALERTS_IDS = List.of();

    static final RecommendationsIn RECOMMENDATIONS_IN = RecommendationsIn.builder()
        .analysisId(SOME_ANALYSIS)
        .alertIds(ALERTS_IDS)
        .build();

    static final Recommendation RECOMMENDATION = Recommendation.newBuilder()
        .setAlert("some_alert")
        .setName("some_name")
        .setRecommendedAction("some_action")
        .setRecommendationComment("some_comment")
        .setRecommendedAt(Timestamps.fromSeconds(1638884728))
        .build();

    static final List<Recommendation> RECOMMENDATIONS = List.of(RECOMMENDATION);

    static final RecommendationsResponse GRPC_RESPONSE = RecommendationsResponse.newBuilder()
        .addAllRecommendations(RECOMMENDATIONS)
        .build();

    static final RecommendationsOut RESPONSE =
        RecommendationsOut.createFrom(GRPC_RESPONSE);
  }
}
