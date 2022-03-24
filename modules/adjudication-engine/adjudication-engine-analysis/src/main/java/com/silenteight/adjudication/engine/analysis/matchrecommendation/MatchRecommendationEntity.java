package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v3.MatchRecommendation;
import com.silenteight.adjudication.api.v3.MatchRecommendationMetadata;
import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.AnalysisMatchRecommendationContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.silenteight.adjudication.engine.analysis.matchrecommendation.transform.AlertMatchMetaDataTransformer.convertMatchMetadata;
import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "MatchRecommendation")
@Builder(access = PACKAGE)
class MatchRecommendationEntity extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "match_recommendation_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long analysisId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long matchId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String recommendedAction;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String comment;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Long alertId;

  RecommendationInfo toRecommendationInfo(MatchContext matchContext) {
    return RecommendationInfo
        .newBuilder()
        .setMatchRecommendation(getRecommendationName())
        .setMatch(getMatchName())
        .setValue(createMatchRecommendation())
        .setMetadata(createRecommendationMetadata(matchContext))
        .build();
  }

  MatchRecommendation createMatchRecommendation() {
    return MatchRecommendation
        .newBuilder()
        .setName(getRecommendationName())
        .setMatch(getMatchName())
        .setCreateTime(fromOffsetDateTime(getCreatedAt()))
        .setRecommendedAction(recommendedAction)
        .setRecommendationComment(comment)
        .build();
  }

  MatchRecommendationMetadata createRecommendationMetadata(MatchContext matchContext) {
    var context = AnalysisMatchRecommendationContext
        .builder()
        .matchContext(matchContext)
        .recommendationId(id)
        .matchId(matchId)
        .analysisId(analysisId)
        .alertId(alertId)
        .build();

    return MatchRecommendationMetadata
        .newBuilder()
        .setName(getRecommendationName() + "/metadata")
        .setMatch(getMatchName())
        .setMatchMetadata(convertMatchMetadata(context))
        .build();
  }

  String getRecommendationName() {
    return "analysis/" + analysisId + "/match-recommendations/" + id;
  }

  String getMatchName() {
    return "alerts/" + alertId + "/matches/" + matchId;
  }
}
