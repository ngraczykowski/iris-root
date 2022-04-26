package com.silenteight.bridge.core.registration.adapter.incoming.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.NotifyBatchErrorCommand;
import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.proto.registration.api.v1.*;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class RegistrationGrpcService extends RegistrationServiceGrpc.RegistrationServiceImplBase {

  private final RegistrationGrpcMapper mapper;
  private final RegistrationFacade registrationFacade;

  @Override
  public void registerBatch(RegisterBatchRequest request, StreamObserver<Empty> responseObserver) {
    log.info("Register batch request received for batch with id [{}].", request.getBatchId());
    var batchId = registrationFacade.register(new RegisterBatchCommand(
        request.getBatchId(),
        request.getAlertCount(),
        request.getBatchMetadata(),
        request.getBatchPriority(),
        request.getIsSimulation()
    ));
    log.info(
        "New batch registered with id [{}], solving [{}].",
        batchId, !request.getIsSimulation());
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void notifyBatchError(
      NotifyBatchErrorRequest request, StreamObserver<Empty> responseObserver) {
    log.info("NotifyBatchError request received for batch with id [{}].", request.getBatchId());
    registrationFacade.notifyBatchError(new NotifyBatchErrorCommand(
        request.getBatchId(),
        request.getErrorDescription(),
        request.getBatchMetadata(),
        request.getIsSimulation()
    ));

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void registerAlertsAndMatches(
      RegisterAlertsAndMatchesRequest request,
      StreamObserver<RegisterAlertsAndMatchesResponse> responseObserver) {
    var batchId = request.getBatchId();
    var alertCount = request.getAlertsWithMatchesCount();
    log.info("Register alerts and matches request received for batch id [{}], alert count [{}].",
        batchId, alertCount);
    var registerAlertsCommand = mapper.toRegisterAlertsCommand(request);
    var alerts = registrationFacade.registerAlertsAndMatches(registerAlertsCommand);
    log.info("Register alerts and matches request completed for batch id [{}].", batchId);

    var registerAlertsAndMatchesResponse = mapper.toRegisterAlertsAndMatchesResponse(alerts);

    responseObserver.onNext(registerAlertsAndMatchesResponse);
    responseObserver.onCompleted();
  }
}
