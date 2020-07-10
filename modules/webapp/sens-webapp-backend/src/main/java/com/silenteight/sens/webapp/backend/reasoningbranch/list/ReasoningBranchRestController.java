package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ListReasoningBranchesRequestDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchFilterDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchesPageDto;
import com.silenteight.sens.webapp.backend.support.Paging;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER_OR_BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchRestController {

  @NonNull
  private final ReasoningBranchesQuery reasoningBranchesQuery;

  @GetMapping("/reasoning-branches")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<ReasoningBranchesPageDto> list(
      @Valid ListReasoningBranchesRequestDto request) {

    log.info(REASONING_BRANCH, "Listing Reasoning Branches. request={}", request);

    ReasoningBranchesPageDto page =
        reasoningBranchesQuery.list(
            new ReasoningBranchFilterDto(request.getAiSolution(), request.getActive()),
            new Paging(request.getPageIndex(), request.getPageSize()));

    log.info(REASONING_BRANCH, "Found a page of Reasoning Branches. total={}", page.getTotal());

    return ok(page);
  }
}
