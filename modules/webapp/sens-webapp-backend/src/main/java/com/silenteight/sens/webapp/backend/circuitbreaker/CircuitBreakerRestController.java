package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class CircuitBreakerRestController {

  @NonNull
  private final DiscrepancyCircuitBreakerQuery query;

  @NonNull
  private final ArchiveDiscrepanciesUseCase archiveUseCase;

  @GetMapping("/discrepant-branches")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepantBranchDto>> listDiscrepantBranches() {
    return ok(query.listDiscrepantBranches());
  }

  @GetMapping("/discrepant-branches/{branchId}/discrepancy-ids")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<Long>> listDiscrepancyIds(@PathVariable String branchId) {
    return ok(query.listDiscrepancyIds(ReasoningBranchIdDto.valueOf(branchId)));
  }

  @GetMapping("/discrepancies")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepancyDto>> listDiscrepancies(
      @RequestParam("id") List<Long> ids) {
    return ok(query.listDiscrepanciesByIds(ids));
  }

  @PatchMapping("/discrepancies/archive")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<Void> archiveDiscrepancies(@RequestBody List<Long> ids) {
    archiveUseCase.apply(new ArchiveCircuitBreakerDiscrepanciesCommand(ids));

    return accepted().build();
  }
}
