package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

@RequiredArgsConstructor
class CancelChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public RejectBulkBranchChangeCommand handle(CancelChangeRequestCommand command) {
    Uuid correlationId = command.getCorrelationId();
    RequestCorrelation.set(toJavaUuid(correlationId));

    UUID bulkChangeId = changeRequestService.cancel(
        command.getChangeRequestId(),
        command.getCancellerUsername(),
        toOffsetDateTime(command.getCancelledAt()));

    return RejectBulkBranchChangeCommand
        .newBuilder()
        .setId(fromJavaUuid(bulkChangeId))
        .setCorrelationId(correlationId)
        .build();
  }
}
