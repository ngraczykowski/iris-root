package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

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

  @Override
  public List<String> findRecommendationAlertNamesByAnalysisName(String analysisName) {
    return crudRecommendationRepository
        .findAlertNamesByAnalysisName(analysisName)
        .stream()
        .map(RecommendationAlertNameProjection::alertName)
        .toList();
  }

  @Override
  public List<RecommendationWithMetadata> findByAnalysisNameAndAlertNameIn(
      String analysisName, List<String> alertNames) {
    return crudRecommendationRepository.findByAnalysisNameAndAlertNameIn(analysisName, alertNames)
        .stream()
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
        .timeout(entity.timeout())
        .build();
  }

  private List<RecommendationEntity> mapToRecommendationEntity(
      List<RecommendationWithMetadata> recommendations) {
    return recommendations.stream()
        .map(recommendation -> RecommendationEntity.builder()
            .name(recommendation.name())
            .alertName(recommendation.alertName())
            .analysisName(recommendation.analysisName())
            .recommendedAction(recommendation.recommendedAction())
            .recommendationComment(recommendation.recommendationComment())
            .recommendedAt(recommendation.recommendedAt().toInstant())
            .payload(recommendation.metadata())
            .timeout(recommendation.timeout())
            .build())
        .toList();
  }
}
