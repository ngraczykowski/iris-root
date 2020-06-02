package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class CreateChangeRequestUseCase {

  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final CreateChangeRequestMessageGateway messageGateway;

  public void apply(@NonNull CreateChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Creating Change Request requested, command={}", command);

    auditTracer.save(new ChangeRequestCreationRequestedEvent(command));
    messageGateway.send(toMessage(command));
  }

  private static com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand toMessage(
      CreateChangeRequestCommand command) {

    return com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand.newBuilder()
        .setBulkChangeId(fromJavaUuid(command.getBulkChangeId()))
        .setMakerUsername(command.getMakerUsername())
        .setMakerComment(command.getMakerComment())
        .setCreatedAt(toTimestamp(command.getCreatedAt()))
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .build();
  }
}
