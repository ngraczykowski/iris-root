package com.silenteight.serp.governance.qa.retention.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.serp.governance.qa.retention.erase.EraseDecisionCommentUseCase;

@Slf4j
@RequiredArgsConstructor
class PersonalInformationExpiredCommandHandler {

  @NonNull
  private final EraseDecisionCommentUseCase eraseDecisionCommentUseCase;

  public void handle(PersonalInformationExpired command) {
    log.debug("Received PersonalInformationExpired command with {} alerts.",
        command.getAlertsCount());
    eraseDecisionCommentUseCase.activate(command.getAlertsList());
    log.debug("Processed PersonalInformationExpired command with {} alerts.",
        command.getAlertsCount());
  }
}
