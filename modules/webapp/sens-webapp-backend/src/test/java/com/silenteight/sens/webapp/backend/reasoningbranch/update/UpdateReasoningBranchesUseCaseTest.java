package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.silenteight.sens.webapp.backend.reasoningbranch.BranchId.of;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.BOTH_CHANGES_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.NO_CHANGES_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.SOLUTION_CHANGE_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.STATUS_CHANGE_COMMAND;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UpdateReasoningBranchesUseCaseTest {

  @Mock
  private ChangeRequestRepository updateRepository;

  @InjectMocks
  private UpdateReasoningBranchesUseCase underTest;

  private static Stream<Arguments> changingCommands() {
    return Stream.of(
        Arguments.of(BOTH_CHANGES_COMMAND),
        Arguments.of(SOLUTION_CHANGE_COMMAND),
        Arguments.of(STATUS_CHANGE_COMMAND));
  }

  @Test
  void success_whenBranchWithNoChanges() {
    Try<Void> actual = underTest.apply(NO_CHANGES_COMMAND);

    assertThat(actual.isSuccess()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("changingCommands")
  void callsMockAndReturnsItResult_whenBranchWithAnyChange(UpdateBranchesCommand command) {
    Try<Void> expected = Try.success(null);
    given(updateRepository.save(command)).willReturn(expected);

    Try<Void> actual = underTest.apply(command);

    then(updateRepository).should().save(command);
    assertThat(actual).isEqualTo(expected);
  }

  static class ReasoningBranchesUpdateServiceFixtures {

    static final BranchId BRANCH_ID = of(1, 2);

    static final UpdateBranchesCommand NO_CHANGES_COMMAND =
        new UpdateBranchesCommand(singletonList(BRANCH_ID), null, null);

    static final UpdateBranchesCommand STATUS_CHANGE_COMMAND =
        new UpdateBranchesCommand(singletonList(BRANCH_ID), null, false);

    static final UpdateBranchesCommand SOLUTION_CHANGE_COMMAND =
        new UpdateBranchesCommand(singletonList(BRANCH_ID), "someSolution", null);

    static final UpdateBranchesCommand BOTH_CHANGES_COMMAND =
        new UpdateBranchesCommand(singletonList(BRANCH_ID), "someSolution", true);

  }
}
