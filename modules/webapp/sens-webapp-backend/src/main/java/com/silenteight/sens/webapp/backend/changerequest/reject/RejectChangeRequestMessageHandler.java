package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

@RequiredArgsConstructor
class RejectChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public RejectBulkBranchChangeCommand handle(RejectChangeRequestCommand command) {
    Uuid correlationId = command.getCorrelationId();
    RequestCorrelation.set(toJavaUuid(correlationId));

    UUID bulkChangeId = changeRequestService.reject(
        command.getChangeRequestId(),
        command.getRejectorUsername(),
        command.getRejectorComment(),
        toOffsetDateTime(command.getRejectedAt()));

    return RejectBulkBranchChangeCommand
        .newBuilder()
        .setId(fromJavaUuid(bulkChangeId))
        .setCorrelationId(correlationId)
        .build();
  }
}
