/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import com.silenteight.simulator.processing.alert.index.domain.State;

import java.util.Map;

public interface SimulationRunningQuery {

  Map<String, Map<State, Long>> indexedAlertStatusesInAnalysis();
}
