package com.silenteight.serp.governance.activation;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Activation extends BaseAggregateRoot implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "activationId", updatable = false)
  private Long id;

  @ToString.Include
  @Column(unique = true, updatable = false)
  private Long decisionGroupId;

  @ToString.Include
  private Long decisionTreeId;

  Activation(Long decisionGroupId, Long decisionTreeId) {
    this.decisionGroupId = decisionGroupId;
    this.decisionTreeId = decisionTreeId;
  }
}
