package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.serp.governance.policy.domain.dto.FeatureConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionDto;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

@Entity
@Table(name = "governance_policy_step_match_condition")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class MatchCondition extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Condition condition;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "governance_policy_step_feature_values",
      joinColumns = @JoinColumn(name = "feature_id"))
  @Column(name = "value")
  private Collection<String> values = new ArrayList<>();

  MatchCondition(String name, Condition condition, Collection<String> values) {
    this.name = name;
    this.condition = condition;
    this.values = values;
  }

  MatchConditionDto toDto() {
    return MatchConditionDto
        .builder()
        .name(getName())
        .condition(getCondition())
        .values(getValues())
        .build();
  }

  FeatureConfigurationDto toConfigurationDto() {
    return FeatureConfigurationDto.builder()
        .name(getName())
        .values(getValues())
        .build();
  }

  public Collection<String> getValues() {
    return new ArrayList<>(values);
  }
}
