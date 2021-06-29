package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.common.GrpcServerExtension;
import com.silenteight.model.api.v1.*;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceImplBase;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private ModelGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedSolvingModelServiceGrpcServer());

    var stub = SolvingModelServiceGrpc.newBlockingStub(
        grpcServerExtension.getChannel());

    underTest = new ModelGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetSolvingModel() {
    //when
    var response = underTest.getSolvingModel();

    //then
    assertEquals("name", response.getName());
    assertEquals("policy", response.getPolicyName());
    assertEquals("strategy", response.getStrategyName());
    assertEquals("feature", response.getFeatures().get(0).getName());
    assertEquals("agentConfig", response.getFeatures().get(0).getAgentConfig());
    assertEquals("category", response.getCategories().get(0));
  }

  @Test
  void shouldExportModel() {
    //when
    var response = underTest.exportModel("solvingModels/45178734-a7d8-4e2f-a0b8-80c5951fb333");

    //then
    assertNotNull(response.getModelJson());
  }

  class MockedSolvingModelServiceGrpcServer extends SolvingModelServiceImplBase {

    @Override
    public void getDefaultSolvingModel(
        Empty request, StreamObserver<SolvingModel> responseObserver) {
      responseObserver.onNext(SolvingModel.newBuilder()
          .setName("name")
          .setPolicyName("policy")
          .setStrategyName("strategy")
          .addAllFeatures(List.of(Feature.newBuilder()
              .setName("feature")
              .setAgentConfig("agentConfig")
              .build()))
          .addAllCategories(List.of("category"))
          .build());
      responseObserver.onCompleted();
    }

    @Override
    public void exportModel(
        ExportModelRequest request, StreamObserver<ExportModelResponse> responseObserver) {
      responseObserver.onNext(ExportModelResponse.newBuilder()
          .setModelJson(ByteString.EMPTY)
          .build());
      responseObserver.onCompleted();
    }
  }
}
