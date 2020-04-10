package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import org.junit.jupiter.api.Test;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class UpdateBranchesCommandTest {

  @Test
  void shouldReturnNoChangesIfNewAiSolutionAndNewIsActiveAreNull() {
    UpdateBranchesCommand command = new UpdateBranchesCommand(emptyList(), null, null);
    assertThat(command.doesNotHaveChanges()).isTrue();
  }

  @Test
  void shouldShowChangesIfNewAiSolutionNullAndActiveNotNull() {
    UpdateBranchesCommand command = new UpdateBranchesCommand(emptyList(), null, FALSE);
    assertThat(command.doesNotHaveChanges()).isFalse();
  }

  @Test
  void shouldShowChangesIfNewAiSolutionNotNullAndActiveNull() {
    UpdateBranchesCommand command = new UpdateBranchesCommand(emptyList(), "SOME_SOLUTION", null);
    assertThat(command.doesNotHaveChanges()).isFalse();
  }
}
