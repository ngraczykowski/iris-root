package com.silenteight.serp.governance.decisiongroup;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class DecisionGroup extends BaseAggregateRoot implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "decisionGroupId")
  @EqualsAndHashCode.Include
  private Long id;

  @ToString.Include
  private String name;

  DecisionGroup(String name) {
    this.name = name;
  }
}
