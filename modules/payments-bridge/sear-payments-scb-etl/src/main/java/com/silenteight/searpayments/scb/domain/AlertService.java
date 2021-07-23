package com.silenteight.searpayments.scb.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.searpayments.scb.domain.Alert.AlertStatus;
import com.silenteight.searpayments.scb.domain.Alert.DamageReason;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AlertService {

  private final AlertRepository alertRepository;

  public Alert getAlert(long id) {
    return alertRepository.getById(id);
  }

  @Transactional
  public Alert save(Alert alert) {
    log.debug(
        "Alert with systemId: {} and messgaeId: {} saved in database", alert.getSystemId(),
        alert.getMessageId());
    return alertRepository.save(alert);
  }

  @Transactional
  public void updateAlertStatus(long alertId, AlertStatus alertStatus) {
    log.debug("updateAlertStatus. id: {}, status: {}", alertId, alertStatus);

    alertRepository.updateStatus(alertId, alertStatus);
  }

  @Transactional
  public void batchUpdateAlertStatusAndDamageReason(
      List<Long> alertIds,
      AlertStatus alertStatus, DamageReason damageReason) {
    alertRepository.batchUpdateStatusAndDamageReason(alertIds, alertStatus, damageReason);
  }

  @Transactional
  public void updateAlertStatusAndOutputStatusNameAndRecommendationSentAt(
      long alertId, AlertStatus alertStatus, String outputStatusName,
      OffsetDateTime recommendationSentAt) {

    if (log.isDebugEnabled())
      log.debug("updateAlertStatusAndOutputStatusNameAndRecommendationSentAt."
              + " id: {}, status: {}, outputStatusName: {}, recommendationSentAt: {}",
          alertId, alertStatus, outputStatusName, recommendationSentAt);

    alertRepository.updateStatusAndRecommendationSentAt(
        alertId, alertStatus, outputStatusName, recommendationSentAt);
  }

  @Transactional
  public void updateAlertStatusAndDamageReason(
      long alertId, AlertStatus alertStatus,
      DamageReason damageReason) {

    if (log.isDebugEnabled())
      log.debug(
          "updateAlertStatusAndDamageReason."
              + " id: {}, status: {}, damageReason: {}",
          alertId, alertStatus, damageReason);

    alertRepository.updateStatusAndDamageReason(alertId, alertStatus, damageReason);
  }
}
