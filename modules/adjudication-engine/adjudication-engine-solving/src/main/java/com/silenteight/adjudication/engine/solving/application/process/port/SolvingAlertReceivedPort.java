/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process.port;

import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

public interface SolvingAlertReceivedPort {

  void handle(final AnalysisAlertsAdded message);
}
