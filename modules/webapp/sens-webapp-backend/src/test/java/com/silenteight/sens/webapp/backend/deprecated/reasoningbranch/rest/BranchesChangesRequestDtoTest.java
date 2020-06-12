package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update.UpdateBranchesCommand;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.*;

class BranchesChangesRequestDtoTest {

  @Test
  void createsCommand() {
    long treeId = 5L;
    List<Long> branchIds = List.of(1L, 2L);
    String aiSolution = "FALSE_POSITIVE";
    Boolean active = TRUE;
    String comment = "Some comment";

    BranchesChangesRequestDto dto =
        new BranchesChangesRequestDto(branchIds, aiSolution, active, comment);

    UpdateBranchesCommand command = dto.toCommand(treeId);
    assertThat(command.getBranchIds()).isEqualTo(branchIds);
    assertThat(command.getNewAiSolution()).contains(aiSolution);
    assertThat(command.getNewStatus()).contains(active);
    assertThat(command.getComment()).contains(comment);
  }
}
