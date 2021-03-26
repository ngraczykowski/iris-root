package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Analysis.Feature;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(access = PACKAGE)
@Entity(name = "AnalysisFeatures")
class AnalysisFeatureEntity {

  @Id
  @Column(name = "analysis_feature_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String feature;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String agentConfig;

  Feature toFeature() {
    return Feature
        .newBuilder()
        .setFeature(getFeature())
        .setAgentConfig(getAgentConfig())
        .build();
  }
}
