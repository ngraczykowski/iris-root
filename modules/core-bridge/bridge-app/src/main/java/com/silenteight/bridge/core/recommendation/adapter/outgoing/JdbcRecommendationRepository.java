package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;

import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcRecommendationRepository implements RecommendationRepository {

  private final CrudRecommendationRepository crudRecommendationRepository;

  @Override
  public void saveAll(List<RecommendationWithMetadata> recommendations) {
    crudRecommendationRepository.saveAll(mapToRecommendationEntity(recommendations));
  }

  @Override
  public List<RecommendationWithMetadata> findByAnalysisName(String analysisName) {
    return crudRecommendationRepository.findByAnalysisName(analysisName).stream()
        .map(this::mapToRecommendationWithMetadata)
        .toList();
  }

  private RecommendationWithMetadata mapToRecommendationWithMetadata(RecommendationEntity entity) {
    return RecommendationWithMetadata.builder()
        .name(entity.name())
        .alertName(entity.alertName())
        .analysisName(entity.analysisName())
        .recommendedAt(entity.recommendedAt().atOffset(ZoneOffset.UTC))
        .recommendationComment(entity.recommendationComment())
        .recommendedAction(entity.recommendedAction())
        .metadata(entity.payload())
        .build();
  }

  private List<RecommendationEntity> mapToRecommendationEntity(
      List<RecommendationWithMetadata> recommendations) {
    return recommendations.stream()
        .map(e -> RecommendationEntity.builder()
            .name(e.name())
            .alertName(e.alertName())
            .analysisName(e.analysisName())
            .recommendedAction(e.recommendedAction())
            .recommendationComment(e.recommendationComment())
            .recommendedAt(e.recommendedAt().toInstant())
            .payload(e.metadata())
            .build())
        .toList();
  }
}
