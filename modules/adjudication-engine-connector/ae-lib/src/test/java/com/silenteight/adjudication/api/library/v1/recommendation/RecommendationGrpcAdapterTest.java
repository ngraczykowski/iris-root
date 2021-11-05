package com.silenteight.adjudication.api.library.v1.recommendation;

import com.silenteight.adjudication.api.library.v1.GrpcServerExtension;
import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceImplBase;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.api.v2.StreamRecommendationsWithMetadataRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RecommendationGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private RecommendationGrpcAdapter underTest;

  @BeforeEach
  public void setup() {
    grpcServerExtension.addService(new MockedRecommendationServiceGrpcServer());
    var stub = RecommendationServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());
    underTest = new RecommendationGrpcAdapter(stub, 1L);
  }

  @Test
  @DisplayName("should get recommendation")
  void shouldGetRecommendation() {
    //given
    var analysis = "analysisName";

    //when
    var response = underTest.getRecommendations(analysis);

    //then
    assertThat(response).hasSize(1);
    response.stream().findFirst().ifPresent(recommendation -> {
      assertThat(recommendation.getName()).isEqualTo("recommendation");
      assertThat(recommendation.getAlert()).isEqualTo("alert");
      assertThat(recommendation.getRecommendedAction()).isEqualTo("recommendationAction");
      assertThat(recommendation.getRecommendationComment()).isEqualTo("recommendationComment");
      assertThat(recommendation.getDate()).isEqualTo(
          OffsetDateTime.parse("1970-01-01T01:00:20+01:00"));
      assertThat(recommendation.getMetadata().getMatchesMetadata().size()).isEqualTo(1);
      assertThat(recommendation.getMetadata().getMatchesMetadata().get(0).getMatch()).isEqualTo(
          "match_1");
      assertThat(recommendation.getMetadata().getMatchesMetadata().get(0).getSolution()).isEqualTo(
          "solution_1");
      assertThat(recommendation.getMetadata().getMatchesMetadata().get(0).getReason())
          .isEqualTo(Map.of("reason_1", "value_1"));
      assertThat(recommendation.getMetadata().getMatchesMetadata().get(0).getCategories())
          .isEqualTo(Map.of("category_1", "category_1_value"));
      assertThat(recommendation
          .getMetadata()
          .getMatchesMetadata()
          .get(0)
          .getFeatures()
          .get("1")
          .getAgentConfig())
          .isEqualTo("config_1");
      assertThat(recommendation
          .getMetadata()
          .getMatchesMetadata()
          .get(0)
          .getFeatures()
          .get("1")
          .getSolution())
          .isEqualTo("solution_1");
      assertThat(recommendation
          .getMetadata()
          .getMatchesMetadata()
          .get(0)
          .getFeatures()
          .get("1")
          .getReason())
          .isEqualTo(Map.of("reason_1", "value_1"));
    });
  }

  static class MockedRecommendationServiceGrpcServer extends RecommendationServiceImplBase {

    @Override
    public void streamRecommendationsWithMetadata(
        StreamRecommendationsWithMetadataRequest request,
        StreamObserver<RecommendationWithMetadata> responseObserver) {
      responseObserver.onNext(RecommendationWithMetadata.newBuilder()
          .setRecommendation(Recommendation.newBuilder()
              .setName("recommendation")
              .setAlert("alert")
              .setRecommendedAction("recommendationAction")
              .setRecommendationComment("recommendationComment")
              .setCreateTime(Timestamp.newBuilder().setSeconds(20).build())
              .build())
          .setMetadata(RecommendationMetadata.newBuilder()
              .setName("metadataName")
              .setAlert("alertName")
              .addAllMatches(
                  List.of(
                      MatchMetadata.newBuilder()
                          .putAllFeatures(
                              Map.of(
                                  "1",
                                  FeatureMetadata.newBuilder()
                                      .setAgentConfig("config_1")
                                      .setSolution("solution_1")
                                      .setReason(Struct.newBuilder()
                                          .putAllFields(
                                              Map.of(
                                                  "reason_1", Value
                                                      .newBuilder()
                                                      .setStringValue("value_1")
                                                      .build()))
                                          .build())
                                      .build()))
                          .putAllCategories(Map.of("category_1", "category_1_value"))
                          .setMatch("match_1")
                          .setSolution("solution_1")
                          .setReason(Struct.newBuilder()
                              .putAllFields(
                                  Map.of(
                                      "reason_1",
                                      Value.newBuilder().setStringValue("value_1").build()))
                              .build())
                          .build()))
              .build())
          .build());
      responseObserver.onCompleted();
    }
  }
}
