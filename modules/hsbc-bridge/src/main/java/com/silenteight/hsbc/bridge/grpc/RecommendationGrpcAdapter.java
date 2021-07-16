package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.api.v2.StreamRecommendationsWithMetadataRequest;
import com.silenteight.hsbc.bridge.common.protobuf.StructMapper;
import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;
import com.silenteight.hsbc.bridge.recommendation.RecommendationWithMetadataDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.*;

import static com.silenteight.hsbc.bridge.common.util.TimestampUtil.toOffsetDateTime;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
class RecommendationGrpcAdapter implements RecommendationServiceClient {

  private final RecommendationServiceBlockingStub recommendationServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = CannotGetRecommendationsException.class)
  public Collection<RecommendationWithMetadataDto> getRecommendations(String analysis) throws
      CannotGetRecommendationsException {
    var recommendations = new ArrayList<RecommendationWithMetadataDto>();
    var grpcRequest = StreamRecommendationsWithMetadataRequest.newBuilder()
        .setRecommendationSource(analysis)
        .build();

    try {
      recommendationServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS)
          .streamRecommendationsWithMetadata(grpcRequest)
          .forEachRemaining(item -> recommendations.add(mapRecommendation(item)));
    } catch (StatusRuntimeException ex) {
      log.error("Cannot get recommendations", ex);
      throw new CannotGetRecommendationsException();
    }

    return recommendations;
  }

  private RecommendationWithMetadataDto mapRecommendation(
      RecommendationWithMetadata recommendationInfo) {
    var recommendation = recommendationInfo.getRecommendation();
    var metadata = recommendationInfo.getMetadata();

    return RecommendationWithMetadataDto.builder()
        .alert(recommendation.getAlert())
        .name(recommendation.getName())
        .date(toOffsetDateTime(recommendation.getCreateTimeOrBuilder()))
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .metadata(mapMetadata(metadata))
        .build();
  }

  private RecommendationMetadata mapMetadata(
      com.silenteight.adjudication.api.v2.RecommendationMetadata metadata) {
    var recommendationMetadata = new RecommendationMetadata();
    recommendationMetadata.setMatchesMetadata(mapMatchMetadata(metadata.getMatchesList()));
    return recommendationMetadata;
  }

  private List<MatchMetadata> mapMatchMetadata(
      List<com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata> metadata) {
    return metadata.stream().map(m -> {
      var matchMetadata = new MatchMetadata();
      matchMetadata.setFeatures(mapFeatures(m.getFeaturesMap()));
      matchMetadata.setCategories(m.getCategoriesMap());
      matchMetadata.setMatch(m.getMatch());
      matchMetadata.setSolution(m.getSolution());
      matchMetadata.setReason(StructMapper.toMap(m.getReason()));

      return matchMetadata;
    }).collect(toList());
  }

  private Map<String, FeatureMetadata> mapFeatures(
      Map<String, com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata> map) {
    return map.entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, e -> mapFeature(e.getValue())));
  }

  private FeatureMetadata mapFeature(
      com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata metadata) {
    var featureMetadata = new FeatureMetadata();
    featureMetadata.setAgentConfig(metadata.getAgentConfig());
    featureMetadata.setSolution(metadata.getSolution());
    featureMetadata.setReason(StructMapper.toMap(metadata.getReason()));
    return featureMetadata;
  }
}

