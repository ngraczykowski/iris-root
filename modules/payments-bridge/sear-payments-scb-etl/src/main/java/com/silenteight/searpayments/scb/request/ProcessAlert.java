package com.silenteight.searpayments.scb.request;

import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.aspects.logging.LogContext.LogContextAction;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.Alert.AlertStatus;
import com.silenteight.searpayments.scb.domain.Alert.DamageReason;
import com.silenteight.searpayments.scb.domain.AlertService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@RequiredArgsConstructor
@Slf4j
class ProcessAlert {

  @NonNull private final Alert alert;
  @NonNull private final AlertService alertService;
  @NonNull private final PrevalidateAlertStrategy prevalidateAlertStrategy;

  @LogContext(LogContextAction.CLEAR_PRESERVE)
  void invoke() {
    establishAlertLogging();
    log.debug("ProcessAlert invoked");

    if (!isAlertDamaged())
      prevalidateAlert();

    persistAlert();

    log.debug("ProcessAlert completed");
  }

  private boolean isAlertDamaged() {
    return alert.getStatus() == AlertStatus.STATE_DAMAGED;
  }

  private void persistAlert() {
    alertService.save(alert);
  }

  private void prevalidateAlert() {
    log.debug(
        "I am about to prevalidate Alert alertId: {}, systemId: {}", alert.getId(),
        alert.getSystemId());
    var result = prevalidateAlertStrategy.validate(alert);
    if (!result.isValid()) {
      var reasonIfInvalid = result.getReasonIfInvalid();
      log.warn("Alert alertId: {}, systemId: {} is invalid. Reason: {}", alert.getId(),
          alert.getSystemId(), reasonIfInvalid);
      alert.clearHits();
      alert.markAsDamaged(new DamageReason(
          reasonIfInvalid.getCode(), reasonIfInvalid.getDescription()));
    } else {
      log.debug("Alert alertId: {}, systemId: {} is valid", alert.getId(),
          alert.getSystemId());
    }
  }

  private void establishAlertLogging() {
    MDC.put("alertId", String.valueOf(alert.getId()));
    MDC.put("systemId", alert.getSystemId());
  }

}
