package com.silenteight.governance.api.library.v1.model;

import com.silenteight.model.api.v1.*;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceImplBase;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class ModelGrpcAdapterErrorHandlingTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private ModelGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedErroneousSolvingModelServiceGrpcServer());

    var stub = SolvingModelServiceGrpc.newBlockingStub(
        grpcServerExtension.getChannel());

    underTest = new ModelGrpcAdapter(stub, 1L);
  }

  @Test
  void handleErrorWhenGetSolvingModel() {
    //when
    GovernanceLibraryRuntimeException thrown =
        Assertions.assertThrows(
            GovernanceLibraryRuntimeException.class, () -> underTest.getSolvingModel()
        );

    //then
    Assertions.assertEquals(
        ModelGrpcAdapter.COULD_NOT_GET_SOLVING_MODEL_ERROR_MSG, thrown.getMessage());
  }

  @Test
  void handleErrorWhenExportModel() {
    //when
    GovernanceLibraryRuntimeException thrown =
        Assertions.assertThrows(
            GovernanceLibraryRuntimeException.class, () -> underTest.exportModel("version")
        );

    //then
    Assertions.assertEquals(ModelGrpcAdapter.COULD_NOT_EXPORT_MODEL_ERROR_MSG, thrown.getMessage());
  }

  @Test
  void handleErrorWhenTransferModel() {
    //when
    GovernanceLibraryRuntimeException thrown =
        Assertions.assertThrows(
            GovernanceLibraryRuntimeException.class, () -> underTest.transferModel(new byte[0])
        );

    //then
    Assertions.assertEquals(ModelGrpcAdapter.COULD_NOT_IMPORT_MODEL_ERROR_MSG, thrown.getMessage());
  }

  @Test
  void handleErrorWhenSendStatus() {
    //when
    GovernanceLibraryRuntimeException thrown =
        Assertions.assertThrows(
            GovernanceLibraryRuntimeException.class, () -> underTest.sendStatus("version")
        );

    //then
    Assertions.assertEquals(
        ModelGrpcAdapter.COULD_NOT_UPDATE_STATUS_ERROR_MSG, thrown.getMessage());
  }

  static class MockedErroneousSolvingModelServiceGrpcServer extends SolvingModelServiceImplBase {

    @Override
    public void getDefaultSolvingModel(
        Empty request, StreamObserver<SolvingModel> responseObserver) {
      responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
    }

    @Override
    public void exportModel(
        ExportModelRequest request, StreamObserver<ExportModelResponse> responseObserver) {
      responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
    }

    @Override
    public void importModel(
        ImportNewModelRequest request, StreamObserver<ImportNewModelResponse> responseObserver) {
      responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
    }

    @Override
    public void useModel(
        UseModelRequest request, StreamObserver<Empty> responseObserver) {
      responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
    }
  }
}
