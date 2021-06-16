package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromOffsetDateTime;
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

  static RecommendationEntity fromAlertSolution(long analysisId, AlertSolution alertSolution) {
    return RecommendationEntity
        .builder()
        .alertId(alertSolution.getAlertId())
        .recommendedAction(alertSolution.getRecommendedAction())
        .analysisId(analysisId)
        .build();
  }

  RecommendationInfo toRecommendationInfo() {
    return RecommendationInfo
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .build();
  }

  Recommendation toRecommendation() {
    return Recommendation
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .setCreateTime(fromOffsetDateTime(getCreatedAt()))
        .setName("recommendation/" + getId())
        .setRecommendedAction(recommendedAction)
        .setRecommendationComment("Recommended action: " + recommendedAction)
        .build();
  }
}
