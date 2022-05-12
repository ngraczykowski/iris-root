package com.silenteight.warehouse.retention.production.personalinformation.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

@Slf4j
@RequiredArgsConstructor
class PersonalInformationExpiredDataHandler {

  public void handle(PersonalInformationExpired personalInformationExpired) {
    log.info("Received PersonalInformationExpired command with {} alerts.",
        personalInformationExpired.getAlertsCount());

    throw new UnsupportedOperationException(
        "PersonalInformationExpired handler not implemented yet.");
  }
}
