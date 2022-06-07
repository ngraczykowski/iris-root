/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process.port;

import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

public interface SolvedAlertPort {

  void generateRecommendation(long alertId, BatchSolveAlertsResponse solvedAlert);
}
