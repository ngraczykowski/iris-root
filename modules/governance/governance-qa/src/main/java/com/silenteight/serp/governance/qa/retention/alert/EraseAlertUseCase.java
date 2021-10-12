package com.silenteight.serp.governance.qa.retention.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.AlertService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseAlertRequest;

import java.util.List;

import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Slf4j
class EraseAlertUseCase {

  static final String PRINCIPAL_NAME = "governance-app";

  @NonNull
  private final AlertService alertService;

  public void activate(List<String> alerts) {
    log.info("Erasing alerts counts {}.", alerts.size());
    alerts.forEach(
        discriminator -> alertService.eraseAlert(
            getDeleteAlertRequest(discriminator)));
  }

  private static EraseAlertRequest getDeleteAlertRequest(String discriminator) {
    return EraseAlertRequest.of(discriminator, PRINCIPAL_NAME, now());
  }
}
