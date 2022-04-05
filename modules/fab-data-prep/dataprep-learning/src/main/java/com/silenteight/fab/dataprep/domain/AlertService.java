package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

  private final AlertRepository alertRepository;

  public Optional<String> getAlertName(String discriminator) {
    return alertRepository.findByDiscriminator(discriminator)
        .map(AlertEntity::getAlertName);
  }

  public void save(String discriminator, String alertName) {
    AlertEntity alertEntity = AlertEntity.builder()
        .discriminator(discriminator)
        .alertName(alertName)
        .build();

    log.debug("Saving: {}", alertEntity);
    alertRepository.save(alertEntity);
  }

  public void deleteAll() {
    alertRepository.deleteAll();
  }
}
