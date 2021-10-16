package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationSource;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationWrapper;
import com.silenteight.sep.base.common.entity.BaseEntity;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import javax.persistence.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "RecommendationEntity")
class RecommendationEntity extends BaseEntity {

  @Id
  @Column(name = "recommendation_id", insertable = false, updatable = false, nullable = false)
  @Setter(PUBLIC)
  @Include
  private UUID id;

  @Column(name = "alert_id", nullable = false, updatable = false)
  @NonNull
  private UUID alertId;

  @Column(name = "recommendation_name", updatable = false)
  private String name;

  @Column(name = "alert_name", updatable = false)
  private String alert;

  @Column(name = "generated_at", nullable = false, updatable = false)
  @NonNull
  private OffsetDateTime generatedAt;

  @Column(name = "recommended_action", updatable = false)
  private String action;

  @Column(name = "recommendation_comment", updatable = false)
  private String comment;

  @Column(name = "recommendation_source", updatable = false, nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private RecommendationSource source;

  @Column(name = "recommendation_reason", updatable = false)
  @Enumerated(EnumType.STRING)
  private RecommendationReason reason;

  RecommendationEntity(RecommendationWrapper wrapper) {
    source = wrapper.getSource();
    alertId = wrapper.getAlertId();

    if (wrapper.hasRecommendation()) {
      Recommendation recommendation = wrapper.getRecommendationWithMetadata().getRecommendation();
      name = recommendation.getName();
      alert = recommendation.getAlert();
      generatedAt = extractCreateTime(recommendation.getCreateTime());
      action = recommendation.getRecommendedAction();
      comment = recommendation.getRecommendationComment();
    } else {
      generatedAt = OffsetDateTime.now();
    }
  }

  private OffsetDateTime extractCreateTime(Timestamp timestamp) {
    checkNotNull(timestamp, "timestamp");
    return Instant
        .ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
        .atOffset(ZoneOffset.UTC);
  }

}
