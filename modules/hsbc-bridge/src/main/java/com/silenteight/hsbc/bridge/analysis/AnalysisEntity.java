package com.silenteight.hsbc.bridge.analysis;

import lombok.*;

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
public class AnalysisEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String analysisName;
  private String datasetName;
  private String solvingModelName;

  AnalysisEntity(String analysisName, String datasetName, String solvingModelName) {
    this.analysisName = analysisName;
    this.datasetName = datasetName;
    this.solvingModelName = solvingModelName;
  }
}
