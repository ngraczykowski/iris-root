package com.silenteight.customerbridge.common.recommendation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@Data
@RequiredArgsConstructor(access = PACKAGE)
@Setter(PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "ScbRecommendation")
class ScbRecommendation extends BaseEntity implements IdentifiableEntity {

  public static final String CONCATENATION_ELEMENT = " - ";
  @Id
  @Column(name = "scbRecommendationId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;
  private String systemId;
  @Nullable
  private String watchlistId;
  private String discriminator;
  private String decision;
  private String comment;
  private OffsetDateTime recommendedAt;

  ScbRecommendation(RecommendationDto recommendationDto) {
    this.systemId = recommendationDto.getExternalId();
    this.watchlistId = null;
    this.discriminator = recommendationDto.getDiscriminator();
    this.decision = recommendationDto.getDecision();
    this.comment = recommendationDto.getComment();
    this.recommendedAt = recommendationDto.getDate();
  }

  RecommendationDto toRecommendationDto() {
    if (watchlistId != null)
      throw new WatchlistLevelConvertRecommendationException();

    return RecommendationDto.builder()
        .externalId(systemId)
        .discriminator(discriminator)
        .decision(decision)
        .comment(comment)
        .date(recommendedAt)
        .build();
  }

  RecommendationDto toOutdatedRecommendationDto(String outdatedRecommendationMessage) {
    if (watchlistId != null)
      throw new WatchlistLevelConvertRecommendationException();

    return RecommendationDto.builder()
        .externalId(systemId)
        .discriminator(discriminator)
        .decision(decision)
        .comment(outdatedRecommendationMessage + CONCATENATION_ELEMENT + comment)
        .date(recommendedAt)
        .build();
  }

  static class WatchlistLevelConvertRecommendationException extends IllegalStateException {

    private static final long serialVersionUID = -3318395529881143331L;

    WatchlistLevelConvertRecommendationException() {
      super("Cannot create RecommendationDto from Watchlist level ScbRecommendation");
    }
  }
}
