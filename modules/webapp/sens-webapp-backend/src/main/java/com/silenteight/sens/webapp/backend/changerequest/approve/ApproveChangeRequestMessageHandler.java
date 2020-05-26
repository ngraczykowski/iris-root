package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;

@RequiredArgsConstructor
// TODO(mmastylo) make it package private in the future
public class ApproveChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public void handle(ApproveChangeRequestCommand message) {
    changeRequestService.approve(
        message.getChangeRequestId(),
        message.getApproverUsername(),
        toOffsetDateTime(message.getApprovedAt()));
  }
}
