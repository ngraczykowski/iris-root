package com.silenteight.sens.webapp.backend.changerequest.reject;

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
public class RejectChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final RejectChangeRequestMessageGateway messageGateway;

  public void apply(@NonNull RejectChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Rejecting Change Request, command={}", command);

    messageGateway.send(toMessage(command));

    auditTracer.save(
        new ChangeRequestRejectionRequestedEvent(
            valueOf(command.getChangeRequestId()),
            "webapp_change_request",
            command));

    log.debug(CHANGE_REQUEST,
        "Rejected Change Request, changeRequestId={}", command.getChangeRequestId());
  }

  private static com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand toMessage(
      RejectChangeRequestCommand command) {

    return com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand.newBuilder()
        .setChangeRequestId(command.getChangeRequestId())
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .setRejectorUsername(command.getRejectorUsername())
        .setRejectorComment(command.getRejectorComment())
        .setRejectedAt(toTimestamp(Instant.now()))
        .build();
  }
}
