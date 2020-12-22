package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BulkBranchChangeTest {

  private final Fixtures fixtures = new Fixtures();

  @Test
  void shouldSetCreatedStateOnInit() {
    assertThat(fixtures.emptyChange.getState()).isEqualTo(State.CREATED);
  }

  @Test
  void shouldSetStateWhenApply() {
    var change = fixtures.emptyChange;

    change.apply();

    assertThat(change.getState()).isEqualTo(State.APPLIED);
    assertThat(change.getCompletedAt()).isNotNull();
  }

  @Test
  void shouldSetStateWhenReject() {
    var change = fixtures.emptyChange;

    change.reject();

    assertThat(change.getState()).isEqualTo(State.REJECTED);
    assertThat(change.getCompletedAt()).isNotNull();
  }

  @Test
  void shouldDoNothingAfterDuplicateApplications() {
    var change = fixtures.emptyChange;

    change.apply();
    var completedAt = change.getCompletedAt();

    change.apply();

    assertThat(change.getCompletedAt()).isEqualTo(completedAt);
    assertThat(change.getState()).isEqualTo(State.APPLIED);
  }

  @Test
  void shouldDoNothingAfterDuplicateRejections() {
    var change = fixtures.emptyChange;

    change.reject();
    var completedAt = change.getCompletedAt();

    change.reject();

    assertThat(change.getCompletedAt()).isEqualTo(completedAt);
    assertThat(change.getState()).isEqualTo(State.REJECTED);
  }

  @Test
  void shouldFailWhenChangeDecision() {
    var change = fixtures.emptyChange;

    change.reject();

    assertThrows(ChangeAlreadyCompletedException.class, change::apply);
  }

  @Test
  void shouldRecordEventWhenApply() {
    var change = fixtures.fullChange;

    change.apply();

    assertThat(change.getDomainEvents())
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(BulkBranchChangeApplied.class, e -> {
          assertThat(e.getCorrelationId()).isEqualTo(change.getCorrelationId());
          assertThat(e.getBulkBranchChangeId()).isEqualTo(change.getBulkBranchChangeId());
          assertThat(e.getReasoningBranchIds())
              .extracting(id ->
                  new ReasoningBranchIdToChange(id.getDecisionTreeId(), id.getFeatureVectorId()))
              .containsExactlyInAnyOrderElementsOf(change.getReasoningBranchIds());
          assertThat(e.getSolutionChange()).hasValue(change.getSolutionChange());
          assertThat(e.getEnablementChange()).hasValue(change.getEnablementChange());
          assertThat(e.getAppliedAt()).isEqualTo(change.getCompletedAt()).isNotNull();
        });
  }

  @Test
  void shouldRecordEventWhenReject() {
    var change = fixtures.emptyChange;

    change.reject();

    assertThat(change.getDomainEvents())
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(BulkBranchChangeRejected.class, e -> {
          assertThat(e.getBulkBranchChangeId()).isEqualTo(change.getBulkBranchChangeId());
          assertThat(e.getRejectedAt()).isEqualTo(change.getCompletedAt()).isNotNull();
        });
  }

  private static class Fixtures {

    BulkBranchChange emptyChange =
        createChange(UUID.randomUUID(), null, null);

    ReasoningBranchIdToChange branch1 = new ReasoningBranchIdToChange(1, 2);
    ReasoningBranchIdToChange branch2 = new ReasoningBranchIdToChange(2, 1);

    BulkBranchChange fullChange =
        createChange(
            UUID.randomUUID(), BranchSolution.BRANCH_FALSE_POSITIVE, true, branch1, branch2);

    @Nonnull
    private static BulkBranchChange createChange(
        UUID uuid,
        @Nullable BranchSolution solution,
        @Nullable Boolean enable,
        ReasoningBranchIdToChange... changes) {

      var change = new BulkBranchChange(uuid, Set.of(changes));

      change.setSolutionChange(solution);
      change.setEnablementChange(enable);

      return change;
    }
  }
}
