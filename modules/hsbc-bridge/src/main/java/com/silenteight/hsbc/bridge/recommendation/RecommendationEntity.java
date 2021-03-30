package com.silenteight.hsbc.bridge.recommendation;

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
@Table(name = "hsbc_bridge_recommendation")
class RecommendationEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String recommendedAction;
  private String recommendationComment;

  RecommendationEntity(StoreRecommendationCommand command) {
    this.recommendedAction = command.getRecommendedAction();
    this.recommendationComment = command.getRecommendationComment();
  }
}
