package com.silenteight.scb.qco;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;

public interface QcoFacade {

  QcoRecommendationAlert process(QcoRecommendationAlert alert);
}
