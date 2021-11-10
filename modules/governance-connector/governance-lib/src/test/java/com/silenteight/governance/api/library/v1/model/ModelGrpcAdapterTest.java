package com.silenteight.governance.api.library.v1.model;

import com.silenteight.model.api.v1.*;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceImplBase;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

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
    Assertions.assertEquals("name", response.getName());
    Assertions.assertEquals("policy", response.getPolicyName());
    Assertions.assertEquals("strategy", response.getStrategyName());
    Assertions.assertEquals("feature", response.getFeatures().get(0).getName());
    Assertions.assertEquals("agentConfig", response.getFeatures().get(0).getAgentConfig());
    Assertions.assertEquals("category", response.getCategories().get(0));
  }

  @Test
  void shouldExportModel() {
    //when
    var response = underTest.exportModel("01.08.2021_8:27:34");

    //then
    Assertions.assertNotNull(response.getModelJson());
    Assertions.assertEquals(1, response.getId());
    Assertions.assertEquals("", response.getName());
    Assertions.assertEquals("01.08.2021_8:27:34", response.getVersion());
  }

  @Test
  void shouldTransferModel() {
    //when
    var response = underTest.transferModel(new byte[0]);

    //then
    Assertions.assertNotNull(response);
  }

  static class MockedSolvingModelServiceGrpcServer extends SolvingModelServiceImplBase {

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
          .setId(1)
          .setName(request.getName())
          .setVersion(request.getVersion())
          .build());
      responseObserver.onCompleted();
    }

    @Override
    public void importModel(
        ImportNewModelRequest request, StreamObserver<ImportNewModelResponse> responseObserver) {
      responseObserver.onNext(ImportNewModelResponse.newBuilder()
          .setModel(request.getModelJson().toString())
          .build());
      responseObserver.onCompleted();
    }

    @Override
    public void useModel(
        UseModelRequest request, StreamObserver<Empty> responseObserver) {
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }

}
