package com.silenteight.adjudication.api.library.v1.analysis;

import com.silenteight.adjudication.api.library.v1.GrpcServerExtension;
import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AnalysisGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private AnalysisGrpcAdapter underTest;

  @BeforeEach
  public void setup() {
    grpcServerExtension.addService(new MockedAnalysisServiceGrpcServer());
    var stub = AnalysisServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());
    underTest = new AnalysisGrpcAdapter(stub, 1L);
  }

  @Test
  @DisplayName("should add dataset")
  void shouldAddDataset() {
    //given
    var request = AddDatasetIn.builder()
        .analysis("analysisName")
        .dataset("datasetName")
        .build();

    //when
    var response = underTest.addDataset(request);

    //then
    assertThat(response.getName()).isEqualTo("analysisName");
    assertThat(response.getAlertsCount()).isZero();
  }

  @Test
  @DisplayName("should create analysis")
  void shouldCreateAnalysis() {
    //given
    var request = CreateAnalysisIn.builder()
        .name("analysisName")
        .policy("policy")
        .strategy("strategy")
        .categories(List.of("category_1", "category_2"))
        .features(List.of(
            FeatureIn.builder().name("feature_1").agentConfig("config_1").build(),
            FeatureIn.builder().name("feature_2").agentConfig("config_2").build()))
        .build();

    //when
    var response = underTest.createAnalysis(request);

    //then
    assertThat(response.getName()).isEqualTo("analysisName");
    assertThat(response.getPolicy()).isEqualTo("policy");
    assertThat(response.getStrategy()).isEqualTo("strategy");
  }

  @Test
  @DisplayName("should get analysis")
  void shouldGetAnalysis() {
    //given
    var analysis = "analysisName";

    //when
    var response = underTest.getAnalysis(analysis);

    //then
    assertThat(response.getAlertsCount()).isZero();
    assertThat(response.getPendingAlerts()).isZero();
  }

  static class MockedAnalysisServiceGrpcServer extends AnalysisServiceImplBase {

    @Override
    public void addDataset(
        AddDatasetRequest request, StreamObserver<AnalysisDataset> responseObserver) {
      responseObserver.onNext(AnalysisDataset.newBuilder().setName("analysisName").build());
      responseObserver.onCompleted();
    }

    @Override
    public void createAnalysis(
        CreateAnalysisRequest request, StreamObserver<Analysis> responseObserver) {
      responseObserver.onNext(createAnalysis());
      responseObserver.onCompleted();
    }

    @Override
    public void getAnalysis(GetAnalysisRequest request, StreamObserver<Analysis> responseObserver) {
      responseObserver.onNext(createAnalysis());
      responseObserver.onCompleted();
    }

    private static Analysis createAnalysis() {
      return Analysis.newBuilder()
          .setName("analysisName")
          .setPolicy("policy")
          .setStrategy("strategy")
          .addAllCategories(List.of("category_1", "category_2"))
          .addAllFeatures(List.of(
              Feature.newBuilder().setFeature("feature_1").setAgentConfig("config_1").build(),
              Feature.newBuilder().setFeature("feature_2").setAgentConfig("config_2").build()))
          .build();
    }
  }
}
