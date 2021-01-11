package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.serp.governance.policy.domain.dto.FeatureDto;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

@Entity
@Table(name = "governance_policy_step_feature")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Feature extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "governance_policy_step_feature_values",
      joinColumns = @JoinColumn(name = "feature_id"))
  @Column(name = "value")
  private Collection<String> values = new ArrayList<>();

  Feature(String name, Collection<String> values) {
    this.name = name;
    this.values = values;
  }

  FeatureDto toFeatureDto() {
    return FeatureDto
        .builder()
        .name(getName())
        .values(getValues())
        .build();
  }

  public Collection<String> getValues() {
    return new ArrayList<>(values);
  }
}
