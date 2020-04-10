package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchesCommand;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static org.assertj.core.api.Assertions.*;

class BranchChangesRequestDtoTest {

  @Test
  void shouldCreateBranchesCommand() {
    BranchChangesRequestDto dto = new BranchChangesRequestDto();
    dto.setActive(FALSE);
    dto.setAiSolution("some clever solution");

    final BranchId branchId1 = BranchId.of(10, 1);
    final BranchId branchId2 = BranchId.of(10, 2);

    final UpdateBranchesCommand command =
        dto.toCommand(List.of(branchId1, branchId2));

    assertThat(command.getBranchIds()).contains(branchId1, branchId2);
    assertThat(command.getNewAiSolution()).contains("some clever solution");
    assertThat(command.getNewStatus()).contains(FALSE);
  }
}
