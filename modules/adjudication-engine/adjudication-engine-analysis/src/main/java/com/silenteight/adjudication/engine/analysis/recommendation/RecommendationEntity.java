package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "Recommendation")
@Builder(access = PACKAGE)
class RecommendationEntity extends BaseEntity {

  @Id
  @Column(name = "recommendation_id", insertable = false, updatable = false, nullable = false)
  // TODO(ahaczewski): Switch to a sequence generator.
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long analysisId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long alertId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String recommendedAction;
}
