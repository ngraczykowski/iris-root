package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.sens.webapp.backend.parser.ReasoningBranchIdParser.parse;
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
  public ResponseEntity<List<DiscrepantBranchDto>> listBranchesWithDiscrepancies(
      @RequestParam("discrepancyStatuses") List<DiscrepancyStatus> discrepancyStatuses) {

    if (discrepancyStatuses.contains(DiscrepancyStatus.ACTIVE))
      return ok(query.listBranchesWithDiscrepancies());
    else {
      return ok(query.listBranchesWithArchivedDiscrepancies());
    }
  }

  @GetMapping("/discrepant-branches/{branchId}/discrepancy-ids")
  public ResponseEntity<List<Long>> listDiscrepancyIds(
      @PathVariable String branchId,
      @RequestParam("discrepancyStatuses") List<DiscrepancyStatus> discrepancyStatuses) {

    if (discrepancyStatuses.contains(DiscrepancyStatus.ACTIVE))
      return ok(query.listDiscrepancyIds(toIdDto(branchId)));
    else {
      return ok(query.listArchivedDiscrepancyIds(toIdDto(branchId)));
    }
  }

  @GetMapping("/discrepancies")
  public ResponseEntity<List<DiscrepancyDto>> listDiscrepancies(
      @RequestParam("id") List<Long> ids) {
    return ok(query.listDiscrepanciesByIds(ids));
  }

  @PatchMapping("/discrepancies/archive")
  public ResponseEntity<Void> archiveDiscrepancies(@RequestBody List<Long> ids) {
    archiveUseCase.apply(new ArchiveCircuitBreakerDiscrepanciesCommand(ids));

    return accepted().build();
  }

  private static ReasoningBranchIdDto toIdDto(String branchId) {
    return ReasoningBranchIdDto.valueOf(parse(branchId));
  }
}
