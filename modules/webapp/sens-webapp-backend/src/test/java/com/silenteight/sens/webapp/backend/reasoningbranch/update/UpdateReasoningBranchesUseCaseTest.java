package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.ReasoningBranchValidator;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.BOTH_CHANGES_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.NO_CHANGES_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.SOLUTION_CHANGE_COMMAND;
import static com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCaseTest.ReasoningBranchesUpdateServiceFixtures.STATUS_CHANGE_COMMAND;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.CollectionUtils.emptyCollection;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateReasoningBranchesUseCaseTest {

  @Mock
  private ChangeRequestRepository updateRepository;
  @Mock
  private AuditTracer auditTracer;

  @Mock
  private ReasoningBranchValidator reasoningBranchValidator;

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

  @Test
  void savesAuditEvent() {
    UpdateBranchesCommand command = BOTH_CHANGES_COMMAND;
    given(updateRepository.save(command)).willReturn(Try.success(null));

    UUID correlationId = RequestCorrelation.id();

    Instant timeBefore = Instant.now();
    underTest.apply(command);
    Instant timeAfter = Instant.now();

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());

    AuditEvent auditEvent = eventCaptor.getValue();

    assertThat(auditEvent.getType()).isEqualTo("ReasoningBranchUpdateRequested");
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(command);
    assertThat(auditEvent.getTimestamp())
        .isAfterOrEqualTo(timeBefore)
        .isBeforeOrEqualTo(timeAfter);
  }

  @Test
  void validatesBranches() {
    UpdateBranchesCommand command = STATUS_CHANGE_COMMAND;
    given(updateRepository.save(command)).willReturn(Try.success(null));

    underTest.apply(command);

    verify(reasoningBranchValidator).validateIds(command.getTreeId(), command.getBranchIds());
  }

  @Test
  void throwsExceptionIfValidatorThrowsException() {
    BranchIdsNotFoundException exception = new BranchIdsNotFoundException(emptyCollection());
    doThrow(exception).when(reasoningBranchValidator).validateIds(anyLong(), anyList());

    assertThatThrownBy(() -> underTest.apply(STATUS_CHANGE_COMMAND)).isEqualTo(exception);
  }

  @Test
  void doesNotSaveIfValidationExceptionThrown() {
    doThrow(new BranchIdsNotFoundException(emptyCollection()))
        .when(reasoningBranchValidator).validateIds(anyLong(), anyList());

    try {
      underTest.apply(STATUS_CHANGE_COMMAND);
    } catch (BranchIdsNotFoundException e) {
      //do nothing
    }

    verifyNoInteractions(updateRepository);
  }

  @Test
  void doesNotAuditIfValidationExceptionThrown() {
    doThrow(new BranchIdsNotFoundException(emptyCollection()))
        .when(reasoningBranchValidator).validateIds(anyLong(), anyList());

    try {
      underTest.apply(STATUS_CHANGE_COMMAND);
    } catch (BranchIdsNotFoundException e) {
      //do nothing
    }

    verifyNoInteractions(auditTracer);
  }

  static class ReasoningBranchesUpdateServiceFixtures {

    static final Long TREE_ID = 1L;
    static final Long BRANCH_ID = 2L;

    static final UpdateBranchesCommand NO_CHANGES_COMMAND =
        new UpdateBranchesCommand(TREE_ID, singletonList(BRANCH_ID), null, null, null);

    static final UpdateBranchesCommand STATUS_CHANGE_COMMAND =
        new UpdateBranchesCommand(TREE_ID, singletonList(BRANCH_ID), null, false, null);

    static final UpdateBranchesCommand SOLUTION_CHANGE_COMMAND =
        new UpdateBranchesCommand(TREE_ID, singletonList(BRANCH_ID), "someSolution", null, null);

    static final UpdateBranchesCommand BOTH_CHANGES_COMMAND =
        new UpdateBranchesCommand(TREE_ID, singletonList(BRANCH_ID), "someSolution", true, null);
  }
}
