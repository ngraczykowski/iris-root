package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
@NoArgsConstructor
public class ApproveChangeRequestUseCase {

  public void apply(@NonNull ApproveChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Approving Change Request, command={}", command);

    log.debug(CHANGE_REQUEST,
        "Approved Change Request, changeRequestId={}", command.getChangeRequestId());
  }
}
