package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.messaging.ApproveChangeRequestMessageGateway;

import java.time.Instant;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class ApproveChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final ApproveChangeRequestMessageGateway messageGateway;

  public void apply(@NonNull ApproveChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Approving Change Request, command={}", command);

    auditTracer.save(
        new ChangeRequestApprovalRequestedEvent(
            valueOf(command.getChangeRequestId()),
            "webapp_change_request",
            command));

    messageGateway.send(toMessage(command));

    log.debug(CHANGE_REQUEST,
        "Approved Change Request, changeRequestId={}", command.getChangeRequestId());
  }

  private static com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand toMessage(
      ApproveChangeRequestCommand command) {

    return com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand.newBuilder()
        .setChangeRequestId(command.getChangeRequestId())
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .setApproverUsername(command.getApproverUsername())
        .setApprovedAt(toTimestamp(Instant.now()))
        .build();
  }
}
