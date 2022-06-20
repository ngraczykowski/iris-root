/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure;

import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;

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
