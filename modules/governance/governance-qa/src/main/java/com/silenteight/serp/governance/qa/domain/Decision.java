package com.silenteight.serp.governance.qa.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.serp.governance.qa.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.domain.DecisionState.VIEWING;
import static java.time.OffsetDateTime.now;
import static java.util.List.of;

@Entity
@Table(name = "governance_qa_decision")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Decision extends BaseAggregateRoot implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private Long alertId;

  @ToString.Include
  @Column(nullable = false)
  private Integer level;

  @ToString.Include
  @Column(nullable = false, length = 16)
  @Enumerated(EnumType.STRING)
  private DecisionState state;

  @ToString.Include
  @Column(nullable = false, length = 64)
  String decidedBy;

  @ToString.Include
  @Column(nullable = false)
  OffsetDateTime decidedAt;

  @ToString.Include
  private String comment;

  @ToString.Include
  @Column(nullable = false)
  private OffsetDateTime updatedAt;

  public boolean canBeProcessed() {
    return of(NEW, VIEWING).contains(state);
  }

  public boolean hasId(Long id) {
    return getId().equals(id);
  }

  public boolean hasState(String state) {
    return getState().toString().equals(state);
  }

  public boolean hasUpdatedAtBefore(OffsetDateTime updatedAt) {
    return getUpdatedAt().isBefore(updatedAt);
  }

  public void resetViewingState() {
    if (hasState(VIEWING.toString())) {
      setState(NEW);
      setUpdatedAt(now());
    }
  }
}
