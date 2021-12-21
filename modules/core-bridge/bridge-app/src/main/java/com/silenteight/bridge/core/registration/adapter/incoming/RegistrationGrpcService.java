package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.RegistrationService;
import com.silenteight.proto.registration.api.v1.*;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import static com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.*;

@GrpcService
@RequiredArgsConstructor
@Slf4j
class RegistrationGrpcService extends RegistrationServiceImplBase {

  private final RegistrationService registrationService;

  @Override
  public void registerBatch(RegisterBatchRequest request, StreamObserver<Empty> responseObserver) {
    var registerBatchCommand =
        new RegisterBatchCommand(request.getBatchId(), request.getAlertCount());

    log.info("Register batch request received: {}", request);
    var batchId = registrationService.register(registerBatchCommand);
    log.info("New batch registered with id: {}", batchId);
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void notifyBatchError(
      NotifyBatchErrorRequest request, StreamObserver<Empty> responseObserver) {
    log.info("NotifyBatchError request received: {}", request);
    registrationService.notifyBatchError(request.getBatchId());

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void registerAlertsAndMatches(
      RegisterAlertsAndMatchesRequest request,
      StreamObserver<RegisterAlertsAndMatchesResponse> responseObserver) {
    // TODO ALL-501, ALL-499
    responseObserver.onNext(RegisterAlertsAndMatchesResponse.getDefaultInstance());
    responseObserver.onCompleted();
  }
}
