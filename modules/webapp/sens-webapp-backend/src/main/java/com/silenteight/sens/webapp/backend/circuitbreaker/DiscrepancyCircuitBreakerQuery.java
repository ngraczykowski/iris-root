package com.silenteight.sens.webapp.backend.circuitbreaker;

import java.util.List;

public interface DiscrepancyCircuitBreakerQuery {

  List<DiscrepantBranchDto> listDiscrepantBranches();

  List<DiscrepancyDto> listDiscrepanciesByIds(List<Long> discrepancyIds);
}
