package com.silenteight.scb.qco;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "silenteight.scb-bridge.qco.enabled", havingValue = "false")
class QcoFacadeMock implements QcoFacade {

  @Override
  public QcoRecommendationAlert process(QcoRecommendationAlert alert) {
    return alert;
  }
}
