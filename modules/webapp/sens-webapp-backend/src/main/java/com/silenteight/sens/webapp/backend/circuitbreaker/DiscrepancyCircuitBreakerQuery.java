package com.silenteight.sens.webapp.backend.circuitbreaker;

import java.util.List;

public interface DiscrepancyCircuitBreakerQuery {

  List<DiscrepantBranchDto> listDiscrepantBranches();

  List<Long> listDiscrepancyIds(ReasoningBranchIdDto branchId);

  List<DiscrepancyDto> listDiscrepanciesByIds(List<Long> discrepancyIds);
}
