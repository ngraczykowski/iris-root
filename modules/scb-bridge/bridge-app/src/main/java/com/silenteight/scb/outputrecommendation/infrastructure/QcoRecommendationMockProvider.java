package com.silenteight.scb.outputrecommendation.infrastructure;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "silenteight.qco.enabled", havingValue = "false")
public class QcoRecommendationMockProvider implements QcoRecommendationProvider {

  @Override
  public QcoRecommendationAlert process(QcoRecommendationAlert alert) {
    return alert;
  }
}
