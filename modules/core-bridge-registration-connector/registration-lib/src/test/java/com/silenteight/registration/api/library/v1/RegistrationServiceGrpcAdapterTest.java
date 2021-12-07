package com.silenteight.registration.api.library.v1;

import com.silenteight.proto.registration.api.v1.*;
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceImplBase;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class RegistrationServiceGrpcAdapterTest {

  @RegisterExtension GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private RegistrationServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedRegistrationServiceGrpcServer());

    var stub = RegistrationServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new RegistrationServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void registerBatch() {
    //when
    var response =
        underTest.registerBatch(RegisterBatchInRequestFixtures.REGISTER_BATCH_IN_REQUEST);

    //then
    Assertions.assertEquals(response, EmptyOut.getInstance());
  }

  @Test
  void notifyBatchError() {
    //when
    var response = underTest.notifyBatchError(NotifyBatchErrorRequestFixtures.NOTIFY_BATCH_ERROR);

    //then
    Assertions.assertEquals(response, EmptyOut.getInstance());
  }

  @Test
  void registerAlertsAndMatches() {
    //when
    var response = underTest.registerAlertsAndMatches(
        RegisterAndMatchesRequestFixtures.REGISTER_ALERTS_AND_MATCHES_IN);

    //then
    Assertions.assertEquals(response.getRegisteredAlertWithMatches().size(), 1);
    Assertions.assertEquals(response, RegisterAndMatchesResponseFixtures.RESPONSE);
  }

  static class MockedRegistrationServiceGrpcServer extends RegistrationServiceImplBase {

    @Override
    public void registerBatch(
        RegisterBatchRequest request, StreamObserver<Empty> responseObserver) {
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void notifyBatchError(
        NotifyBatchErrorRequest request, StreamObserver<Empty> responseObserver) {
      responseObserver.onNext(Fixtures.EMPTY_GRPC_RESPONSE);
      responseObserver.onCompleted();
    }

    @Override
    public void registerAlertsAndMatches(
        RegisterAlertsAndMatchesRequest request,
        StreamObserver<RegisterAlertsAndMatchesResponse> responseObserver) {
      responseObserver.onNext(RegisterAndMatchesResponseFixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    static final Empty EMPTY_GRPC_RESPONSE = Empty.newBuilder().build();
  }
}
