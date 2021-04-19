package com.silenteight.serp.governance.changerequest.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;

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
}
