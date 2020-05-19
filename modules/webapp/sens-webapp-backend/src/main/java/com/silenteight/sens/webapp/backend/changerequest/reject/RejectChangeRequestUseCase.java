package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
public class RejectChangeRequestUseCase {

  public void apply(@NonNull RejectChangeRequestCommand command) {
    log.debug(CHANGE_REQUEST, "Rejecting Change Request, command={}", command);

    log.debug(CHANGE_REQUEST,
        "Rejected Change Request, changeRequestId={}", command.getChangeRequestId());
  }
}
