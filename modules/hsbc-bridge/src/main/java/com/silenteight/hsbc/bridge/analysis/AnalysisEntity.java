package com.silenteight.hsbc.bridge.analysis;

import lombok.*;

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;
import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_analysis")
class AnalysisEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String dataset;
  private String policy;
  private String strategy;

  AnalysisEntity(AnalysisDto analysis, String dataset) {
    this.name = analysis.getName();
    this.policy = analysis.getPolicy();
    this.strategy = analysis.getStrategy();
    this.dataset = dataset;
  }

  AnalysisDto toAnalysisDto() {
    return AnalysisDto.builder()
        .id(this.id)
        .name(this.name)
        .policy(this.policy)
        .strategy(this.strategy)
        .dataset(this.dataset)
        .build();
  }
}
