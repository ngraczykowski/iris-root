package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
public class GetRecommendationUseCase {

  private final RecommendationMapper recommendationMapper;
  private final RecommendationRepository repository;
  private final ObjectMapper objectMapper;

  public RecommendationWithMetadataDto getRecommendation(
      @NonNull GetRecommendationRequest request) {
    var findResult = repository.findByAlert(request.getAlert());

    if (findResult.isEmpty()) {
      throw new RecommendationNotFoundException(request.getAlert());
    }

    var entity = findResult.get();
    var recommendation = recommendationMapper.getRecommendationValue(
        entity.getRecommendedAction(), request.getExtendedAttribute5());

    var builder = RecommendationWithMetadataDto.builder()
        .alert(entity.getAlert())
        .recommendationComment(entity.getRecommendationComment())
        .recommendedAction(recommendation)
        .date(entity.getRecommendedAt())
        .name(entity.getName());

    if (entity.hasMetadata()) {
      builder.metadata(mapMetadata(entity.getMetadata()));
    }

    return builder.build();
  }

  private RecommendationMetadata mapMetadata(RecommendationMetadataEntity metadata) {
    var objectNode = metadata.getPayload();
    return tryToConvertMetadata(objectNode).orElse(new RecommendationMetadata());
  }

  private Optional<RecommendationMetadata> tryToConvertMetadata(ObjectNode objectNode) {
    try {
      return of(objectMapper.treeToValue(objectNode, RecommendationMetadata.class));
    } catch (JsonProcessingException e) {
      log.error("Recommendation metadata conversion error", e);
      return empty();
    }
  }

  public interface GetRecommendationRequest {

    String getAlert();

    String getExtendedAttribute5();
  }
}
