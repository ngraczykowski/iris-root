/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GnsSolutionMapper {

  private final Map<Integer, AnalystSolution> stateIdMappings = new HashMap<>();

  public GnsSolutionMapper(Map<AnalystSolution, List<Integer>> analystSolutionsMap) {
    analystSolutionsMap.forEach((k, v) -> v.forEach(id -> stateIdMappings.put(id, k)));
  }

  public AnalystSolution mapSolution(int type) {
    return stateIdMappings.getOrDefault(type, AnalystSolution.ANALYST_OTHER);
  }
}
