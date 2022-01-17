package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.serp.governance.policy.domain.dto.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step_feature_logic")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class FeatureLogic extends BaseEntity implements StepSearchCriteriaMatcher {

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
    return FeatureLogicDto.builder().toFulfill(getCount()).features(featuresToDto()).build();
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

  TransferredFeatureLogicDto toTransferredDto() {
    TransferredFeatureLogicDto dto = new TransferredFeatureLogicDto();
    dto.setToFulfill(getCount());
    dto.setMatchConditions(toTransferredMatchConditionDtos());
    return dto;
  }

  private List<TransferredMatchConditionDto> toTransferredMatchConditionDtos() {
    return getFeatures()
        .stream()
        .map(MatchCondition::toTransferredDto)
        .collect(toList());
  }

  FeatureLogic cloneFeatureLogic() {
    return new FeatureLogic(getCount(), cloneFeatures(getFeatures()));
  }

  private Collection<MatchCondition> cloneFeatures(Collection<MatchCondition> features) {
    return features
        .stream()
        .map(MatchCondition::cloneMatchCondition)
        .collect(toList());
  }

  @Override
  public boolean match(StepSearchCriteriaDto criteria) {
    return features.stream().anyMatch(mc -> mc.match(criteria));
  }
}
