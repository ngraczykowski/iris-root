package com.silenteight.serp.governance.changerequest.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotInPendingStateException;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestOperationNotAllowedException;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.REJECTED;
import static java.time.OffsetDateTime.now;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class ChangeRequest extends BaseAggregateRoot implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(name = "change_request_id", nullable = false)
  private UUID changeRequestId;

  @NonNull
  @ToString.Include
  @Column(name = "model_name", nullable = false)
  private String modelName;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChangeRequestState state = PENDING;

  @ToString.Include
  @Column(name = "created_by", updatable = false, nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "creator_comment", updatable = false, nullable = false)
  private String creatorComment;

  @ToString.Include
  @Column(name = "decided_by")
  private String decidedBy;

  @ToString.Include
  @Column(name = "decider_comment")
  private String deciderComment;

  @ToString.Include
  @Column(name = "decided_at")
  private OffsetDateTime decidedAt;

  ChangeRequest(UUID changeRequestId, String modelName, String createdBy, String creatorComment) {
    this.changeRequestId = changeRequestId;
    this.modelName = modelName;
    this.createdBy = createdBy;
    this.creatorComment = creatorComment;
  }

  boolean isInState(ChangeRequestState state) {
    return getState() == state;
  }

  boolean hasChangeRequestId(UUID changeRequestId) {
    return getChangeRequestId() == changeRequestId;
  }

  void approve(String username, String comment) {
    if (isNotInPendingState())
      throw new ChangeRequestNotInPendingStateException(getChangeRequestId());

    if (isCreatedBy(username))
      throw new ChangeRequestOperationNotAllowedException(getChangeRequestId());

    state = APPROVED;
    decidedBy = username;
    deciderComment = comment;
    decidedAt = now();
  }

  void reject(String username, String comment) {
    if (isNotInPendingState())
      throw new ChangeRequestNotInPendingStateException(getChangeRequestId());

    if (isCreatedBy(username))
      throw new ChangeRequestOperationNotAllowedException(getChangeRequestId());

    state = REJECTED;
    decidedBy = username;
    deciderComment = comment;
    decidedAt = now();
  }

  void cancel(String username) {
    if (isNotInPendingState())
      throw new ChangeRequestNotInPendingStateException(getChangeRequestId());

    state = CANCELLED;
    decidedBy = username;
    decidedAt = now();
  }

  private boolean isNotInPendingState() {
    return state != PENDING;
  }

  private boolean isCreatedBy(String username) {
    return createdBy.equals(username);
  }

  ChangeRequestDto toDto() {
    return ChangeRequestDto.builder()
        .id(getChangeRequestId())
        .createdBy(getCreatedBy())
        .createdAt(getCreatedAt())
        .creatorComment(getCreatorComment())
        .decidedBy(getDecidedBy())
        .deciderComment(getDeciderComment())
        .decidedAt(getDecidedAt())
        .state(getState())
        .modelName(getModelName())
        .build();
  }
}
