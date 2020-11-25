package com.silenteight.serp.governance.branch;

import lombok.*;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import javax.persistence.*;

@Entity
@Data
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Branch extends BaseAggregateRoot implements IdentifiableEntity {

  @Setter
  @Id
  @Column(name = "branchId", updatable = false)
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(updatable = false)
  private Long decisionTreeId;

  @Column(updatable = false)
  private Long featureVectorId;

  @ToString.Include
  private OffsetDateTime lastUsedAt;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  private BranchSolution solution = BranchSolution.BRANCH_NO_DECISION;

  @ToString.Include
  private boolean enabled = true;

  Branch(long decisionTreeId, long featureVectorId) {
    this.decisionTreeId = decisionTreeId;
    this.featureVectorId = featureVectorId;
  }

  void apply(BranchChange branchChange) {
    branchChange.getEnabledChange().ifPresent(this::setEnabled);
    branchChange.getSolutionChange().ifPresent(this::setSolution);
  }
}
