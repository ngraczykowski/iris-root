package com.silenteight.bridge.core.reports.adapter.outgoing.crossmodule;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.bridge.core.recommendation.domain.model.FeatureMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.reports.domain.model.FeatureMetadataDto;
import com.silenteight.bridge.core.reports.domain.model.MatchMetadataDto;
import com.silenteight.bridge.core.reports.domain.model.RecommendationMetadataDto;
import com.silenteight.bridge.core.reports.domain.model.RecommendationWithMetadataDto;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ReportsRecommendationServiceAdapter implements RecommendationService {

  private final RecommendationFacade recommendationFacade;

  @Override
  public List<RecommendationWithMetadataDto> getRecommendations(String analysisName) {
    var command = new GetRecommendationCommand(analysisName, List.of());
    return recommendationFacade.getRecommendations(command)
        .stream()
        .map(this::toRecommendationWithMetadataDto)
        .toList();
  }

  private RecommendationWithMetadataDto toRecommendationWithMetadataDto(
      RecommendationWithMetadata recommendationWithMetadata) {
    return RecommendationWithMetadataDto.builder()
        .name(recommendationWithMetadata.name())
        .alertName(recommendationWithMetadata.alertName())
        .analysisName(recommendationWithMetadata.analysisName())
        .recommendedAction(recommendationWithMetadata.recommendedAction())
        .recommendationComment(recommendationWithMetadata.recommendationComment())
        .recommendedAt(recommendationWithMetadata.recommendedAt())
        .metadata(getRecommendationMetadataDto(recommendationWithMetadata))
        .timeout(recommendationWithMetadata.timeout())
        .build();
  }

  private RecommendationMetadataDto getRecommendationMetadataDto(
      RecommendationWithMetadata recommendationWithMetadata) {
    return Optional.ofNullable(recommendationWithMetadata.metadata())
        .map(RecommendationMetadata::matchMetadata)
        .map(matchMetadata -> new RecommendationMetadataDto(matchMetadata.stream()
            .map(this::toMetadataDto)
            .toList()))
        .orElseGet(() -> new RecommendationMetadataDto(List.of()));
  }

  private MatchMetadataDto toMetadataDto(MatchMetadata matchMetadata) {
    return MatchMetadataDto.builder()
        .match(matchMetadata.match())
        .solution(matchMetadata.solution())
        .reason(matchMetadata.reason())
        .categories(matchMetadata.categories())
        .features(matchMetadata.features()
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Entry::getKey, this::toFeatureMetadataDto))
        )
        .build();
  }

  private FeatureMetadataDto toFeatureMetadataDto(Entry<String, FeatureMetadata> entry) {
    return FeatureMetadataDto.builder()
        .agentConfig(entry.getValue().agentConfig())
        .solution(entry.getValue().solution())
        .reason(entry.getValue().reason())
        .build();
  }
}
