/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.port.outgoing;

import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;

public interface QcoWarehouseReportAdapter {

  void send(QcoRecommendationMatch recommendationMatch, MatchSolution matchSolution);
}
