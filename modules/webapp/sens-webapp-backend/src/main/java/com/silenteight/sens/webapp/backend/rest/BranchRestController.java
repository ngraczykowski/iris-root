package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.branch.BranchModelDto;
import com.silenteight.sens.webapp.backend.presentation.dto.branch.BranchResponseDto;
import com.silenteight.sens.webapp.backend.presentation.dto.branch.BranchSearchFilterDto;
import com.silenteight.sens.webapp.backend.presentation.dto.branch.details.BranchDetailsDto;
import com.silenteight.sens.webapp.backend.support.Paging;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
public class BranchRestController {

  @GetMapping("/branches")
  public ResponseEntity<BranchResponseDto> getBranches(
      BranchSearchFilterDto filter, Paging paging) {

    BranchResponseDto response = new BranchResponseDto(
        0, new BranchModelDto(emptyList()), emptyList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/decision-tree/{decisionTreeId}/branch/{matchGroupId}")
  public ResponseEntity<BranchDetailsDto> getBranch(
      @PathVariable long decisionTreeId, @PathVariable long matchGroupId) {

    BranchDetailsDto response = null;
    return ResponseEntity.ok(response);
  }

  @PostMapping("/decision-tree/{decisionTreeId}/branch/{matchGroupId}/review")
  public ResponseEntity<Void> reviewBranch(
      @PathVariable long decisionTreeId, @PathVariable long matchGroupId) {

    return ResponseEntity.noContent().build();
  }
}
