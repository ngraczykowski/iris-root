package com.silenteight.scb.outputrecommendation.infrastructure;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;

public interface QcoRecommendationProvider {

  QcoRecommendationAlert process(QcoRecommendationAlert alert);
}
