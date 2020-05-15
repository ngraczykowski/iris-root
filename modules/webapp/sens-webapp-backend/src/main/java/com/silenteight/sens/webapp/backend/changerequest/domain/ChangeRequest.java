package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.*;

import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotInPendingStateException;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;

@Entity
@Data
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class ChangeRequest {

  @Id
  @Column(name = "changeRequestId", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private UUID bulkChangeId;

  @ToString.Include
  @Column(nullable = false)
  private String state = PENDING.name();

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private String makerUsername;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private String makerComment;

  @ToString.Include
  private String approverUsername;

  @Column(updatable = false, nullable = false)
  @Access(AccessType.FIELD)
  @CreationTimestamp
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Setter(AccessLevel.PACKAGE)
  @Column(insertable = false)
  @Access(AccessType.FIELD)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  @Getter(AccessLevel.NONE)
  @Version
  @Column(nullable = false)
  @Access(AccessType.FIELD)
  private Integer version;

  ChangeRequest(UUID bulkChangeId, String makerUsername, String makerComment) {
    this.bulkChangeId = bulkChangeId;
    this.makerUsername = makerUsername;
    this.makerComment = makerComment;
  }

  void approve(String username) {
    validatePendingState();

    state = APPROVED.name();
    approverUsername = username;
  }

  void reject(String username) {
    validatePendingState();

    state = REJECTED.name();
    approverUsername = username;
  }

  private void validatePendingState() {
    if (isNotInPendingState())
      throw new ChangeRequestNotInPendingStateException(bulkChangeId);
  }

  private boolean isNotInPendingState() {
    return !state.equals(PENDING.name());
  }
}
