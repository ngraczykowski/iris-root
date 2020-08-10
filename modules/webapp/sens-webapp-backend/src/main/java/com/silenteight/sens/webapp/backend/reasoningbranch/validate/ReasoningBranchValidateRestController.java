package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdAndSignatureDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdsAndSignaturesDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdsValidationResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchValidateRestController {

  @NonNull
  private final ReasoningBranchValidator reasoningBranchValidator;

  @PutMapping("/decision-trees/{treeId}/branches/validate")
  @PreAuthorize("isAuthorized('VALIDATE_REASONING_BRANCHES')")
  public ResponseEntity<BranchIdsValidationResponseDto> validate(
      @PathVariable long treeId, @RequestBody @Valid BranchIdsAndSignaturesDto branchIdsDto) {

    Map<Long, String> branchIdsMap = reasoningBranchValidator.validate(
        treeId,
        branchIdsDto.getBranchIds(),
        branchIdsDto.getFeatureVectorSignatures());

    return ok(branchIdsValidationResponseDtoOf(branchIdsMap));
  }

  private static BranchIdsValidationResponseDto branchIdsValidationResponseDtoOf(
      Map<Long, String> branchIdsMap) {
    List<BranchIdAndSignatureDto>
        branchIdsResponse =
        branchIdsMap.entrySet().stream()
            .map(e -> new BranchIdAndSignatureDto(e.getKey(), e.getValue()))
            .collect(toList());

    return new BranchIdsValidationResponseDto(branchIdsResponse);
  }
}
