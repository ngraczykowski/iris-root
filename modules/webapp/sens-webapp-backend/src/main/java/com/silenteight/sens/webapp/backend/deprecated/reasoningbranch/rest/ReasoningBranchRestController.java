package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update.UpdateReasoningBranchesUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.common.rest.Authority.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController("deprecatedReasoningBranchRestController")
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchRestController {

  @NonNull
  private final ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @NonNull
  private final ReasoningBranchesQuery reasoningBranchesQuery;

  @NonNull
  private final UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase;


  @GetMapping("/decision-trees/{treeId}/branches/{branchNo}")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<BranchDetailsDto> details(
      @PathVariable long treeId, @PathVariable long branchNo) {
    log.info(REASONING_BRANCH, "Requesting Reasoning Branch details. treeId={}, branchNo={}",
        treeId, branchNo);

    Optional<BranchDetailsDto> branchDetails =
        reasoningBranchDetailsQuery.findByTreeIdAndBranchId(treeId, branchNo);

    branchDetails.ifPresentOrElse(
        details -> log.info(REASONING_BRANCH, "Found Reasoning Branch details. {}", details),
        () -> log.error(REASONING_BRANCH, "Reasoning Branch details not found."));

    return branchDetails
        .map(ResponseEntity::ok)
        .orElseGet(() -> notFound().build());
  }

  @PostMapping("/decision-trees/{treeId}/branches")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<List<BranchDto>> list(
      @PathVariable long treeId, @RequestBody ListBranchesRequestDto request) {
    log.info(REASONING_BRANCH, "Listing Reasoning Branches. treeId={}, request={}",
        treeId, request);

    List<BranchDto> branches =
        reasoningBranchesQuery.findBranchByTreeIdAndBranchIds(treeId, request.getBranchIds());

    log.info(REASONING_BRANCH, "Found {} Reasoning Branches.", branches.size());

    return ok(branches);
  }

  @PatchMapping("/decision-trees/{treeId}/branches")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<Void> update(
      @PathVariable long treeId, @RequestBody BranchesChangesRequestDto branchesChanges) {
    log.info(REASONING_BRANCH, "Updating Reasoning Branches. treeId={}, dto={}",
        treeId, branchesChanges);

    return updateReasoningBranchesUseCase
        .apply(branchesChanges.toCommand(treeId))
        .map(ResponseEntity::ok)
        .onSuccess(ignored -> log.info(REASONING_BRANCH, "Reasoning Branches updated"))
        .onFailure(ex -> log.error(REASONING_BRANCH, "Could not update Reasoning Branches", ex))
        .get();
  }
}
