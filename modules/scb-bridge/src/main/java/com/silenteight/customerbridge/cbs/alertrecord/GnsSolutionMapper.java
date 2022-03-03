package com.silenteight.customerbridge.cbs.alertrecord;

import com.silenteight.proto.serp.v1.alert.AnalystSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_OTHER;

public class GnsSolutionMapper {

  private final Map<Integer, AnalystSolution> stateIdMappings = new HashMap<>();

  public GnsSolutionMapper(Map<AnalystSolution, List<Integer>> analystSolutionsMap) {
    analystSolutionsMap.forEach((k, v) -> v.forEach(id -> stateIdMappings.put(id, k)));
  }

  public AnalystSolution mapSolution(int type) {
    return stateIdMappings.getOrDefault(type, ANALYST_OTHER);
  }
}
