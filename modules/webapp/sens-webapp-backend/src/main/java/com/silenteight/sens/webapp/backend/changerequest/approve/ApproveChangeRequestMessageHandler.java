package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;
import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

@RequiredArgsConstructor
class ApproveChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public ApplyBulkBranchChangeCommand handle(ApproveChangeRequestCommand command) {
    Uuid correlationId = command.getCorrelationId();
    RequestCorrelation.set(toJavaUuid(correlationId));

    UUID bulkChangeId = changeRequestService.approve(
        command.getChangeRequestId(),
        command.getApproverUsername(),
        toOffsetDateTime(command.getApprovedAt()));

    return ApplyBulkBranchChangeCommand
        .newBuilder()
        .setId(fromJavaUuid(bulkChangeId))
        .setCorrelationId(correlationId)
        .build();
  }
}
