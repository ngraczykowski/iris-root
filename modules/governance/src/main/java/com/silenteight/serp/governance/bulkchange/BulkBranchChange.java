package com.silenteight.serp.governance.bulkchange;

import lombok.*;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.*;

import static java.time.OffsetDateTime.now;
import static java.util.stream.Collectors.toSet;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
class BulkBranchChange extends BaseAggregateRoot implements IdentifiableEntity {

  private static final String ID_COLUMN = "id";

  @Id
  @Column(name = ID_COLUMN, nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private UUID bulkBranchChangeId;

  @Enumerated(EnumType.STRING)
  private State state = State.CREATED;

  @Nullable
  private OffsetDateTime completedAt;

  @Nullable
  @Enumerated(value = EnumType.STRING)
  private BranchSolution solutionChange;

  @Nullable
  private Boolean enablementChange;

  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = ID_COLUMN))
  private Set<ReasoningBranchIdToChange> reasoningBranchIds = new HashSet<>();

  // BS
  @Transient
  private UUID correlationId;

  BulkBranchChange(UUID bulkBranchChangeId, Set<ReasoningBranchIdToChange> reasoningBranchIds) {
    this.bulkBranchChangeId = bulkBranchChangeId;
    this.reasoningBranchIds = reasoningBranchIds;
  }

  void apply() {
    if (getState() == State.APPLIED)
      return;

    completeWith(State.APPLIED);

    recordEvent(this.createAppliedEvent());
  }

  void reject() {
    if (getState() == State.REJECTED)
      return;

    completeWith(State.REJECTED);

    recordEvent(this.createRejectEvent());
  }

  private void completeWith(State requestedState) {
    if (getState() != State.CREATED)
      throw new ChangeAlreadyCompletedException(
          getBulkBranchChangeId(), getState(), requestedState);

    setState(requestedState);
    setCompletedAt(now());
  }

  private BulkBranchChangeApplied createAppliedEvent() {
    return BulkBranchChangeApplied.builder()
        .correlationId(correlationId)
        .bulkBranchChangeId(getBulkBranchChangeId())
        .enablementChange(getEnablementChange())
        .solutionChange(getSolutionChange())
        .reasoningBranchIds(getBranchIdsToApply())
        .appliedAt(getCompletedAt())
        .build();
  }

  private Set<ReasoningBranchIdToApply> getBranchIdsToApply() {
    return getReasoningBranchIds()
        .stream()
        .map(ReasoningBranchIdToChange::toApply)
        .collect(toSet());
  }

  private BulkBranchChangeRejected createRejectEvent() {
    return BulkBranchChangeRejected.builder()
        .bulkBranchChangeId(getBulkBranchChangeId())
        .rejectedAt(getCompletedAt())
        .build();
  }

  enum State {
    CREATED, APPLIED, REJECTED
  }
}
