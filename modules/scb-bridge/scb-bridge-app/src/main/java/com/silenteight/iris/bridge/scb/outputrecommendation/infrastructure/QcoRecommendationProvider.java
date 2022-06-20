/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure;

import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;

public interface QcoRecommendationProvider {

  QcoRecommendationAlert process(QcoRecommendationAlert alert);
}
