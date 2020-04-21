package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCase;
import com.silenteight.sens.webapp.backend.security.Authority;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.REASONING_BRANCH;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ReasoningBranchRestController {

  @NonNull
  private final ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @NonNull
  private final ReasoningBranchesQuery reasoningBranchesQuery;

  @NonNull
  private final UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase;

  @NonNull
  private final AuditLog auditLog;

  @GetMapping("/decision-trees/{treeId}/branches/{branchNo}")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<BranchDetailsDto> details(
      @PathVariable long treeId, @PathVariable long branchNo) {
    auditLog.logInfo(REASONING_BRANCH,
        "Requesting Reasoning Branch details. treeId={}, branchNo={}",
        treeId, branchNo);

    Optional<BranchDetailsDto> branchDetails =
        reasoningBranchDetailsQuery.findByTreeIdAndBranchId(treeId, branchNo);

    branchDetails.ifPresentOrElse(
        details -> auditLog.logInfo(
            REASONING_BRANCH, "Found Reasoning Branch details. {}", details),
        () -> auditLog.logError(REASONING_BRANCH, "Reasoning Branch details not found."));

    return branchDetails
        .map(ResponseEntity::ok)
        .orElseGet(() -> notFound().build());
  }

  @PostMapping("/decision-trees/{treeId}/branches")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<List<BranchDto>> list(
      @PathVariable long treeId, @RequestBody ListBranchesRequestDto request) {
    auditLog.logInfo(REASONING_BRANCH, "Listing Reasoning Branches. treeId={}, request={}",
        treeId, request);

    List<BranchDto> branches =
        reasoningBranchesQuery.findByTreeIdAndBranchIds(treeId, request.getBranchIds());

    auditLog.logInfo(REASONING_BRANCH, "Found {} Reasoning Branches.", branches.size());

    return ok(branches);
  }

  @PatchMapping("/decision-trees/{treeId}/branches")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<Void> update(
      @PathVariable long treeId, @RequestBody BranchesChangesRequestDto branchesChanges) {
    auditLog.logInfo(REASONING_BRANCH, "Updating Reasoning Branches. treeId={}, dto={}",
        treeId, branchesChanges);

    return updateReasoningBranchesUseCase
        .apply(branchesChanges.toCommand(treeId))
        .map(ResponseEntity::ok)
        .onSuccess(ignored -> auditLog.logInfo(REASONING_BRANCH, "Reasoning Branches updated"))
        .onFailure(
            ex -> auditLog.logError(REASONING_BRANCH, "Could not update Reasoning Branches", ex))
        .recover(BranchNotFoundException.class, e -> notFound().build())
        .recover(AiSolutionNotSupportedException.class, e -> status(BAD_REQUEST).build())
        .getOrElse(() -> status(INTERNAL_SERVER_ERROR).build());
  }
}
