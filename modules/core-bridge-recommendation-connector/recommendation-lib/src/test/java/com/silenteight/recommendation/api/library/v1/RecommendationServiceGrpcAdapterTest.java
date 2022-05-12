package com.silenteight.recommendation.api.library.v1;

import com.silenteight.proto.recommendation.api.v1.*;
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus;
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceImplBase;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
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

  @Test
  void streamRecommendations() {
    //when
    var response = underTest.streamRecommendations(Fixtures.RECOMMENDATIONS_IN);

    //then
    var result = new ArrayList<>();
    response.forEachRemaining(result::add);

    Assertions.assertEquals(result.size(), 1);
    Assertions.assertEquals(result.get(0), Fixtures.GRPC_STREAM_RESPONSE);
  }

  static class MockedRecommendationServiceGrpcServer extends RecommendationServiceImplBase {

    @Override
    public void getRecommendations(
        RecommendationsRequest request, StreamObserver<RecommendationsResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }

    @Override
    public void streamRecommendations(
        RecommendationsRequest request, StreamObserver<RecommendationResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_STREAM_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    static final String ALERT_ID = "alert_id";
    static final String ALERT_NAME = "alert/a8d8ffd5-1a25-4385-a599-a9e54c307cd3";
    static final String ALERT_METADATA = "some_metadata";
    static final String ALERT_ERROR_MSG = "some_error_message";

    static final String ANALYSIS_NAME = "some_analysis";
    static final List<String> ALERTS_NAMES = List.of(ALERT_NAME);

    static final RecommendationsIn RECOMMENDATIONS_IN = RecommendationsIn.builder()
        .analysisName(ANALYSIS_NAME)
        .alertNames(ALERTS_NAMES)
        .build();

    static final Alert ALERT = Alert.newBuilder()
        .setId(ALERT_ID)
        .setStatus(AlertStatus.SUCCESS)
        .setMetadata(ALERT_METADATA)
        .setErrorMessage(ALERT_ERROR_MSG)
        .build();

    static final String MATCH_ID = "match/f1a322eb-9903-4dea-bce3-86ec7e325098";
    static final String MATCH_RECOMMENDED_ACTION = "match_recommended_action";
    static final String MATCH_RECOMMENDED_COMMENT = "match_recommended_comment";
    static final String MATCH_STEP_ID = "steps/192247c0-751b-457e-a9b3-a4a1bc2dd0b5";
    static final String MATCH_FV_SIGNATURE = "xcR2QVZCxU6bCBRxeghvvOa0BSc=";
    static final Struct FEATURES = Struct.newBuilder()
        .putFields("name", Value.newBuilder().setStringValue("EXACT_MATCH").build())
        .putFields("gender", Value.newBuilder().setStringValue("MATCH").build())
        .build();

    static final Match MATCH = Match.newBuilder()
        .setId(MATCH_ID)
        .setRecommendedAction(MATCH_RECOMMENDED_ACTION)
        .setRecommendationComment(MATCH_RECOMMENDED_COMMENT)
        .setStepId(MATCH_STEP_ID)
        .setFvSignature(MATCH_FV_SIGNATURE)
        .setFeatures(FEATURES)
        .build();

    static final Recommendation RECOMMENDATION = Recommendation.newBuilder()
        .setName("some_name")
        .setRecommendedAction("some_action")
        .setRecommendationComment("some_comment")
        .setRecommendedAt(Timestamps.fromSeconds(1638884728))
        .setAlert(ALERT)
        .addAllMatches(List.of(MATCH))
        .build();

    static final List<Recommendation> RECOMMENDATIONS = List.of(RECOMMENDATION);

    static final RecommendationsResponse GRPC_RESPONSE = RecommendationsResponse.newBuilder()
        .addAllRecommendations(RECOMMENDATIONS)
        .build();

    static final RecommendationResponse GRPC_STREAM_RESPONSE = RecommendationResponse.newBuilder()
        .setRecommendation(RECOMMENDATION)
        .build();

    static final RecommendationsOut RESPONSE =
        RecommendationsOut.createFrom(GRPC_RESPONSE);
  }
}
