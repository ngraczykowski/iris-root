package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchUseCase;
import com.silenteight.sens.webapp.backend.security.Authority;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchRestController {

  @NonNull
  private final ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @NonNull
  private final UpdateReasoningBranchUseCase updateReasoningBranchUseCase;

  @GetMapping("/decision-trees/{treeId}/branches/{branchNo}")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<BranchDetailsDto> details(
      @PathVariable long treeId, @PathVariable long branchNo) {
    log.debug(REASONING_BRANCH, "Requesting Reasoning Branch details. treeId={}, branchNo={}",
        treeId, branchNo);

    Optional<BranchDetailsDto> branchDetails =
        reasoningBranchDetailsQuery.findByTreeIdAndBranchId(treeId, branchNo);

    branchDetails.ifPresentOrElse(
        details -> log.debug(REASONING_BRANCH, "Found Reasoning Branch details. {}", details),
        () -> log.error(REASONING_BRANCH, "Reasoning Branch details not found.")
    );
    return branchDetails
        .map(ResponseEntity::ok)
        .orElseGet(() -> notFound().build());
  }

  @PatchMapping("/decision-trees/{treeId}/branches/{branchNo}")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<Void> update(
      @PathVariable long treeId,
      @PathVariable long branchNo,
      @RequestBody BranchChangesRequestDto branchChanges) {
    log.debug(REASONING_BRANCH, "Updating Reasoning Branch. treeId={}, branchNo={}",
        treeId, branchNo);

    return updateReasoningBranchUseCase
        .apply(branchChanges.toCommand(BranchId.of(treeId, branchNo)))
        .map(ResponseEntity::ok)
        .onSuccess(ignored -> log.debug(REASONING_BRANCH, "Reasoning Branch updated"))
        .onFailure(ex -> log.error(REASONING_BRANCH, "Could not update Reasoning Branch", ex))
        .recover(BranchNotFoundException.class, e -> notFound().build())
        .recover(AiSolutionNotSupportedException.class, e -> status(BAD_REQUEST).build())
        .getOrElse(() -> status(INTERNAL_SERVER_ERROR).build());
  }
}
