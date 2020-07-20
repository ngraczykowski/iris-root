package com.silenteight.sens.webapp.backend.circuitbreaker;

import java.util.List;

public interface DiscrepancyCircuitBreakerQuery {

  List<DiscrepantBranchDto> listBranchesWithDiscrepancies();

  List<DiscrepantBranchDto> listBranchesWithArchivedDiscrepancies();

  List<Long> listDiscrepancyIds(ReasoningBranchIdDto branchId);

  List<Long> listArchivedDiscrepancyIds(ReasoningBranchIdDto branchId);

  List<DiscrepancyDto> listDiscrepanciesByIds(List<Long> discrepancyIds);
}
