package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.LearningData;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertService {

  private final AlertRepository alertRepository;

  boolean isLearningAlert(LearningData learningData) {
    return alertRepository.findByDiscriminator(getDiscriminator(learningData)).isPresent();
  }

  void save(LearningData learningData) {
    var discriminator = getDiscriminator(learningData);
    var alertName = learningData.getAlertName();

    AlertEntity alertEntity = AlertEntity.builder()
        .discriminator(discriminator)
        .alertName(alertName)
        .build();

    log.debug("Saving: {}", alertEntity);
    alertRepository.save(alertEntity);
  }

  private static String getDiscriminator(LearningData learningData) {
    return learningData.getSystemId() + "|" + learningData.getMessageId();
  }
}
