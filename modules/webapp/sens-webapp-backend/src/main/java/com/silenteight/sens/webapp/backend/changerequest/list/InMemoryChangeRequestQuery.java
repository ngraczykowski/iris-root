package com.silenteight.sens.webapp.backend.changerequest.list;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.rest.ChangeRequestQuery;
import com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchAiSolutionDto;
import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ChangeRequestDto;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchAiSolutionDto.FALSE_POSITIVE;
import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchStatusDto.ACTIVE;
import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchStatusDto.DISABLED;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;

@Slf4j
class InMemoryChangeRequestQuery implements ChangeRequestQuery {

  @Override
  public List<ChangeRequestDto> pending() {
    log.debug(CHANGE_REQUEST, "Listing pending Change Requests (mock)");

    return List.of(
        ChangeRequestDto.builder()
            .id(1L)
            .createdBy("Business Operator #1")
            .createdAt(OffsetDateTime.now().minusDays(3))
            .affectedBranchesCount(35)
            .branchAiSolution(FALSE_POSITIVE)
            .branchStatus(ACTIVE)
            .comment("Increase efficiency by 20% on Asia markets")
            .build(),
        ChangeRequestDto.builder()
            .id(2L)
            .createdBy("Business Operator #2")
            .createdAt(OffsetDateTime.now().minusHours(4))
            .affectedBranchesCount(null)
            .branchAiSolution(BranchAiSolutionDto.NO_CHANGE)
            .branchStatus(DISABLED)
            .comment("Disable redundant RBs based on analyses from 2020.04.02")
            .build()
    );
  }
}
