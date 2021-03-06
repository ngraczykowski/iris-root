package com.silenteight.hsbc.bridge.recommendation;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_recommendation")
class RecommendationEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String recommendedAction;
  private String recommendationComment;
  private String alert;
  private String name;
  private OffsetDateTime recommendedAt;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "recommendation_metadata_id")
  private RecommendationMetadataEntity metadata;

  @Transient
  boolean hasMetadata() {
    return Objects.nonNull(metadata);
  }

  RecommendationEntity(RecommendationWithMetadataDto recommendation) {
    this.recommendedAction = recommendation.getRecommendedAction();
    this.recommendationComment = recommendation.getRecommendationComment();
    this.alert = recommendation.getAlert();
    this.name = recommendation.getName();
    this.recommendedAt = recommendation.getDate();
  }
}
