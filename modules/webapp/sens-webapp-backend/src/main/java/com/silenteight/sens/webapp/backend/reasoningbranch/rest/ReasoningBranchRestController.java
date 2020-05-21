package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCase;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.ReasoningBranchValidator;
import com.silenteight.sens.webapp.common.rest.Authority;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
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

  @NonNull
  private final ReasoningBranchValidator reasoningBranchValidator;

  @GetMapping("/decision-trees/{treeId}/branches/{branchNo}")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
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
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
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
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
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

  @PutMapping("/decision-trees/{treeId}/branches/validate")
  @PreAuthorize(Authority.BUSINESS_OPERATOR)
  public ResponseEntity<BranchIdsValidationResponseDto> validate(
      @PathVariable long treeId, @RequestBody @Valid BranchIdsAndSignaturesDto branchIdsDto) {

    Map<Long, String> branchIdsMap = reasoningBranchValidator.validate(
        treeId,
        branchIdsDto.getBranchIds(),
        branchIdsDto.getFeatureVectorSignatures());

    return ok(branchIdsValidationResponseDtoOf(branchIdsMap));
  }

  private BranchIdsValidationResponseDto branchIdsValidationResponseDtoOf(
      Map<Long, String> branchIdsMap) {
    List<BranchIdAndSignatureDto> branchIdsResponse =
        branchIdsMap.entrySet().stream()
            .map(e -> new BranchIdAndSignatureDto(e.getKey(), e.getValue()))
            .collect(toList());

    return new BranchIdsValidationResponseDto(branchIdsResponse);
  }
}
