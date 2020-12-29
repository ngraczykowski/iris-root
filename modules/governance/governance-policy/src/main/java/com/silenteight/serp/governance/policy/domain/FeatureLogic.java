package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step_feature_logic")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class FeatureLogic extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private Integer count;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "feature_logic_id", updatable = false, nullable = false)
  private Collection<Feature> features = new ArrayList<>();

  FeatureLogic(Integer count, Collection<Feature> features) {
    this.count = count;
    this.features = features;
  }
}
