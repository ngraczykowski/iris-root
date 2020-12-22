package com.silenteight.serp.governance.decisiontree;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class DecisionTree extends BaseAggregateRoot implements IdentifiableEntity {

  @Setter
  @Getter
  @Id
  @Column(name = "decisionTreeId", updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ToString.Include
  @EqualsAndHashCode.Include
  private Long id;

  @Getter
  @Column(unique = true)
  @ToString.Include
  private String name;

  @Getter
  private boolean defaultForNewGroups = false;

  DecisionTree(String name) {
    this.name = name;
  }

  DecisionTree enableForAutomaticDecisionGroupActivation() {
    this.setDefaultForNewGroups(true);
    return this;
  }

  DecisionTree disableForAutomaticDecisionGroupActivation() {
    this.setDefaultForNewGroups(false);
    return this;
  }
}

