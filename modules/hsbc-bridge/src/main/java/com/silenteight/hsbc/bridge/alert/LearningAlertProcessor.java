package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertSender.SendOption;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.AGENTS;
import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.WAREHOUSE;
import static com.silenteight.hsbc.bridge.alert.AlertStatus.LEARNING_COMPLETED;

@RequiredArgsConstructor
@Slf4j
public class LearningAlertProcessor {

  private final AlertRepository repository;
  private final AlertSender alertSender;

  @Transactional
  public void process(@NonNull Collection<Long> alertIds) {
    log.info("Processing learning alerts, size={}", alertIds.size());

    var alerts = repository.findByIdIn(alertIds);

    alerts.forEach(a -> {
      a.setStatus(LEARNING_COMPLETED);
      repository.save(a);
    });

    sendAlerts(alerts);
  }

  private void sendAlerts(List<AlertEntity> alerts) {
    alertSender.send(alerts, new SendOption[] { AGENTS, WAREHOUSE });
  }
}
