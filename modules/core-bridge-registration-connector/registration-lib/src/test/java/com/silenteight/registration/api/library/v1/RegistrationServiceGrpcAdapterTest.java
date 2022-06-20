package com.silenteight.registration.api.library.v1;

import com.silenteight.proto.registration.api.v1.*;
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceImplBase;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class RegistrationServiceGrpcAdapterTest {

  @RegisterExtension GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private RegistrationServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedRegistrationServiceGrpcServer());

    var stub = RegistrationServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new RegistrationServiceGrpcAdapter(stub, 10L);
  }

  @MethodSource("registerBatchParameters")
  @ParameterizedTest(name = "{index} should register batch with {2} in request.")
  void shouldRegisterBatch(RegisterBatchIn request, EmptyOut expectedResponse, String description) {
    //when
    var response =
        underTest.registerBatch(request);

    //then
    Assertions.assertEquals(response, expectedResponse);
  }

  @Test
  @DisplayName("should notify batch error")
  void shouldNotifyBatchError() {
    //when
    var response = underTest.notifyBatchError(NotifyBatchErrorRequestFixtures.NOTIFY_BATCH_ERROR);

    //then
    Assertions.assertEquals(response, EmptyOut.getInstance());
  }

  @Test
  @DisplayName("should register alerts and matches")
  void shouldRegisterAlertsAndMatches() {
    //when
    var response = underTest.registerAlertsAndMatches(
        RegisterAlertAndMatchesRequestFixtures.REGISTER_ALERTS_AND_MATCHES_IN);

    //then
    Assertions.assertEquals(response.getRegisteredAlertWithMatches().size(), 1);
    Assertions.assertEquals(response, RegisterAlertAndMatchesResponseFixtures.RESPONSE);
  }

  private static Stream<Arguments> registerBatchParameters() {
    return Stream.of(
        Arguments.of(
            RegisterBatchInRequestFixtures.REGISTER_BATCH_IN,
            EmptyOut.getInstance(),
            "all fields filled"),
        Arguments.of(
            RegisterBatchInRequestFixtures.REGISTER_BATCH_IN_WITH_NULL_PRIORITY,
            EmptyOut.getInstance(),
            "null priority"),
        Arguments.of(
            RegisterBatchInRequestFixtures.REGISTER_BATCH_IN_WITH_NULL_SIMULATION,
            EmptyOut.getInstance(),
            "null simulation flag"),
        Arguments.of(
            RegisterBatchInRequestFixtures.REGISTER_BATCH_IN_WITH_NULL_SIMULATION_AND_NULL_PRIORITY,
            EmptyOut.getInstance(),
            "null simulation flag and null priority")
    );
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
      responseObserver.onNext(RegisterAlertAndMatchesResponseFixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    static final Empty EMPTY_GRPC_RESPONSE = Empty.newBuilder().build();
  }
}
