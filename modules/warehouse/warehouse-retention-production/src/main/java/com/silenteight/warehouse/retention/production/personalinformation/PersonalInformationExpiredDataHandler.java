package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

@Slf4j
@RequiredArgsConstructor
class PersonalInformationExpiredDataHandler {

  @NonNull
  private final ErasePersonalInformationUseCase erasePersonalInformationUseCase;

  public void handle(PersonalInformationExpired personalInformationExpired) {
    log.info("Received PersonalInformationExpired command with {} alerts.",
        personalInformationExpired.getAlertsCount());

    erasePersonalInformationUseCase.activate(personalInformationExpired.getAlertsList());
    log.debug("Processed PersonalInformationExpired command with {} alerts.",
        personalInformationExpired.getAlertsCount());
  }
}
