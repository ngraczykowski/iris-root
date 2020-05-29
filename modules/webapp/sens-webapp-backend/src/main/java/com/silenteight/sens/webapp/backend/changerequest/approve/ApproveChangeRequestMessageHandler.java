package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

@RequiredArgsConstructor
// TODO(mmastylo) make it package private in the future
public class ApproveChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public void handle(ApproveChangeRequestCommand command) {
    RequestCorrelation.set(toJavaUuid(command.getCorrelationId()));

    changeRequestService.approve(
        command.getChangeRequestId(),
        command.getApproverUsername(),
        toOffsetDateTime(command.getApprovedAt()));
  }
}
