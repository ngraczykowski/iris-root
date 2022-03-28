package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertService {

  private final AlertRepository alertRepository;

  boolean isLearningAlert(RegisteredAlert registeredAlert) {
    return alertRepository.findByDiscriminator(getDiscriminator(registeredAlert)).isPresent();
  }

  void save(RegisteredAlert registeredAlert) {
    var discriminator = getDiscriminator(registeredAlert);
    var alertName = registeredAlert.getAlertName();

    AlertEntity alertEntity = AlertEntity.builder()
        .discriminator(discriminator)
        .alertName(alertName)
        .build();

    log.debug("Saving: {}", alertEntity);
    alertRepository.save(alertEntity);
  }

  private static String getDiscriminator(RegisteredAlert registeredAlert) {
    return registeredAlert.getSystemId() + "|" + registeredAlert.getMessageId();
  }
}
