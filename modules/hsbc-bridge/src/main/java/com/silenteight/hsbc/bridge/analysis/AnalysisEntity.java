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
  private String datasetName;
  private String policy;
  private String strategy;

  AnalysisEntity(AnalysisDto analysis, String datasetName) {
    this.name = analysis.getName();
    this.policy = analysis.getPolicy();
    this.strategy = analysis.getStrategy();
    this.datasetName = datasetName;
  }
}
