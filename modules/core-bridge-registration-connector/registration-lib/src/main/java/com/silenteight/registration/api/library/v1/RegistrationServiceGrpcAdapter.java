package com.silenteight.registration.api.library.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceGrpcAdapter implements RegistrationServiceClient {

  public static final String COULD_NOT_REGISTER_BATCH = "Couldn't register batch";
  public static final String COULD_NOT_NOTIFY_BATCH_ERROR = "Couldn't notify batch error";
  public static final String COULD_NOT_REGISTER_ALERTS_AND_MATCHES =
      "Couldn't register alerts and matches";

  private final RegistrationServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public EmptyOut registerBatch(RegisterBatchIn request) {
    return Try.of(() -> getStub().registerBatch(request.toRegisterBatchRequest()))
        .map(empty -> EmptyOut.getInstance())
        .onFailure(e -> log.error(COULD_NOT_REGISTER_BATCH, e))
        .onSuccess(emptyOut -> log.debug("Successfully registered batch"))
        .getOrElseThrow(e -> new RegistrationLibraryException(COULD_NOT_REGISTER_BATCH, e));
  }

  @Override
  public EmptyOut notifyBatchError(NotifyBatchErrorIn request) {
    return Try.of(() -> getStub().notifyBatchError(request.toNotifyBatchErrorRequest()))
        .map(empty -> EmptyOut.getInstance())
        .onFailure(e -> log.error(COULD_NOT_NOTIFY_BATCH_ERROR, e))
        .onSuccess(emptyOut -> log.debug("Successfully notified batch error"))
        .getOrElseThrow(e -> new RegistrationLibraryException(COULD_NOT_NOTIFY_BATCH_ERROR, e));
  }

  @Override
  public RegisterAlertsAndMatchesOut registerAlertsAndMatches(RegisterAlertsAndMatchesIn request) {
    return Try
        .of(() -> getStub().registerAlertsAndMatches(request.toRegisterAlertsAndMatchesRequest()))
        .map(RegisterAlertsAndMatchesOut::createFrom)
        .onFailure(e -> log.error(COULD_NOT_REGISTER_ALERTS_AND_MATCHES, e))
        .onSuccess(emptyOut -> log.debug("Successfully registered alerts and matches"))
        .getOrElseThrow(
            e -> new RegistrationLibraryException(COULD_NOT_REGISTER_ALERTS_AND_MATCHES, e));
  }

  private RegistrationServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
