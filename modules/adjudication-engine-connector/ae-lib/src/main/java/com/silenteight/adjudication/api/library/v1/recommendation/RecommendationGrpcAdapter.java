package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.util.StructMapperUtil;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.api.v2.StreamRecommendationsWithMetadataRequest;

import io.grpc.StatusRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.silenteight.adjudication.api.library.v1.util.TimeStampUtil.toOffsetDateTime;

@RequiredArgsConstructor
@Slf4j
public class RecommendationGrpcAdapter implements RecommendationServiceClient {

  private final RecommendationServiceBlockingStub recommendationServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public Collection<RecommendationWithMetadataOut> getRecommendations(String analysis) {
    var recommendations = new ArrayList<RecommendationWithMetadataOut>();
    var grpcRequest = StreamRecommendationsWithMetadataRequest.newBuilder()
        .setRecommendationSource(analysis)
        .build();

    try {
      getStub().streamRecommendationsWithMetadata(grpcRequest)
          .forEachRemaining(item -> recommendations.add(mapRecommendation(item)));
    } catch (StatusRuntimeException e) {
      log.error("Cannot get recommendations", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot get recommendations", e);
    }

    return recommendations;
  }

  private RecommendationWithMetadataOut mapRecommendation(
      RecommendationWithMetadata recommendationInfo) {
    var recommendation = recommendationInfo.getRecommendation();
    var metadata = recommendationInfo.getMetadata();

    return RecommendationWithMetadataOut.builder()
        .alert(recommendation.getAlert())
        .name(recommendation.getName())
        .date(toOffsetDateTime(recommendation.getCreateTimeOrBuilder()))
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .metadata(mapMetadata(metadata))
        .build();
  }

  private RecommendationMetadataOut mapMetadata(RecommendationMetadata metadata) {
    var recommendationMetadata = new RecommendationMetadataOut();
    recommendationMetadata.setMatchesMetadata(mapMatchMetadata(metadata.getMatchesList()));

    return recommendationMetadata;
  }

  private List<MatchMetadataOut> mapMatchMetadata(
      List<RecommendationMetadata.MatchMetadata> metadata) {
    return metadata.stream().map(m -> {
      var matchMetadata = new MatchMetadataOut();
      matchMetadata.setFeatures(mapFeatures(m.getFeaturesMap()));
      matchMetadata.setCategories(m.getCategoriesMap());
      matchMetadata.setMatch(m.getMatch());
      matchMetadata.setSolution(m.getSolution());
      matchMetadata.setReason(StructMapperUtil.toMap(m.getReason()));

      return matchMetadata;
    }).collect(Collectors.toList());
  }

  private Map<String, FeatureMetadataOut> mapFeatures(
      Map<String, RecommendationMetadata.FeatureMetadata> map) {
    return map.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> mapFeature(e.getValue())));
  }

  private FeatureMetadataOut mapFeature(RecommendationMetadata.FeatureMetadata metadata) {
    var featureMetadata = new FeatureMetadataOut();
    featureMetadata.setAgentConfig(metadata.getAgentConfig());
    featureMetadata.setSolution(metadata.getSolution());
    featureMetadata.setReason(StructMapperUtil.toMap(metadata.getReason()));

    return featureMetadata;
  }

  private RecommendationServiceBlockingStub getStub() {
    return recommendationServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
