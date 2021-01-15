package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionDto;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step_feature_logic")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  @OneToMany(cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "feature_logic_id", updatable = false, nullable = false)
  private Collection<MatchCondition> features = new ArrayList<>();

  FeatureLogic(Integer count, Collection<MatchCondition> features) {
    this.count = count;
    this.features = features;
  }

  FeatureLogicDto toDto() {
    return FeatureLogicDto.builder().count(getCount()).features(featuresToDto()).build();
  }

  private Collection<MatchConditionDto> featuresToDto() {
    return getFeatures().stream().map(MatchCondition::toDto).collect(toList());
  }

  FeatureLogicConfigurationDto toConfigurationDto() {
    return FeatureLogicConfigurationDto
        .builder().count(getCount()).features(featuresToConfigurationDto()).build();
  }

  private Collection<MatchConditionConfigurationDto> featuresToConfigurationDto() {
    return getFeatures().stream().map(MatchCondition::toConfigurationDto).collect(toList());
  }
}
