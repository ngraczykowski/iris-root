package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class CircuitBreakerRestController {

  @NonNull
  private final DiscrepancyCircuitBreakerQuery query;

  @GetMapping("/discrepant-branches")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepantBranchDto>> listDiscrepantBranches() {
    return ok(query.listDiscrepantBranches());
  }

  @GetMapping("/discrepancies")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepancyDto>> listDiscrepancies(
      @RequestParam("id") List<Long> ids) {
    return ok(query.listDiscrepanciesByIds(ids));
  }
}


