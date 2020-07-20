package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.sens.webapp.backend.parser.ReasoningBranchIdParser.parse;
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

  @GetMapping(value = "/discrepant-branches", params = "withArchivedDiscrepancies=false")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepantBranchDto>> listBranchesWithDiscrepancies() {
    return ok(query.listBranchesWithDiscrepancies());
  }

  @GetMapping(value = "/discrepant-branches", params = "withArchivedDiscrepancies=true")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<DiscrepantBranchDto>> listBranchesWithArchivedDiscrepancies() {
    return ok(query.listBranchesWithArchivedDiscrepancies());
  }

  @GetMapping(value = "/discrepant-branches/{branchId}/discrepancy-ids", params = "archived=false")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<Long>> listDiscrepancyIds(@PathVariable String branchId) {
    return ok(query.listDiscrepancyIds(toIdDto(branchId)));
  }

  @GetMapping(value = "/discrepant-branches/{branchId}/discrepancy-ids", params = "archived=true")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<Long>> listArchivedDiscrepancyIds(@PathVariable String branchId) {
    return ok(query.listArchivedDiscrepancyIds(toIdDto(branchId)));
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

  private static ReasoningBranchIdDto toIdDto(String branchId) {
    return ReasoningBranchIdDto.valueOf(parse(branchId));
  }
}
