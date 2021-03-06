package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(access = PACKAGE)
@Entity
class AnalysisFeature {

  @Id
  @Column(name = "analysis_feature_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @Include
  @NonNull
  private Long agentConfigFeatureId;
}
