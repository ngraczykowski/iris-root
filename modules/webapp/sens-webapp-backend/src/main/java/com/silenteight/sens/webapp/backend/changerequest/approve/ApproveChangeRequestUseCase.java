package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@NoArgsConstructor
public class ApproveChangeRequestUseCase {

  public long apply(@NonNull ApproveChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Approving Change Request, command={}", command);

    //TODO: Business logic of approve action
    long approvalId = 123;

    log.debug(CHANGE_REQUEST, "Approved Change Request, approvalId={}", approvalId);
    return approvalId;
  }
}
