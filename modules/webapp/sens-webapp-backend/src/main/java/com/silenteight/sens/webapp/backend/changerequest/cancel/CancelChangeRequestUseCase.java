package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import java.time.Instant;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class CancelChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final CancelChangeRequestMessageGateway messageGateway;

  public void apply(@NonNull CancelChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Cancelling Change Request, command={}", command);

    messageGateway.send(toMessage(command));

    auditTracer.save(
        new ChangeRequestCancellationRequestedEvent(
            valueOf(command.getChangeRequestId()),
            "webapp_change_request",
            command));

    log.debug(CHANGE_REQUEST,
        "Cancelled Change Request, changeRequestId={}", command.getChangeRequestId());
  }

  private static com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand toMessage(
      CancelChangeRequestCommand command) {

    return com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand.newBuilder()
        .setChangeRequestId(command.getChangeRequestId())
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .setCancellerUsername(command.getCancellerUsername())
        .setCancelledAt(toTimestamp(Instant.now()))
        .build();
  }
}
