package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.util.StructMapperUtil;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.api.v2.StreamRecommendationsWithMetadataRequest;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.silenteight.adjudication.api.library.v1.util.TimeStampUtil.toOffsetDateTime;

@RequiredArgsConstructor
@Slf4j
public class RecommendationGrpcAdapter implements RecommendationServiceClient {

  private static final String CANNOT_GET_RECOMMENDATIONS = "Cannot get recommendations";
  private final RecommendationServiceBlockingStub recommendationServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public Collection<RecommendationWithMetadataOut> getRecommendations(String analysis) {
    var grpcRequest = StreamRecommendationsWithMetadataRequest.newBuilder()
        .setRecommendationSource(analysis)
        .build();

    return Try.of(() -> getStub().streamRecommendationsWithMetadata(grpcRequest))
        .map(this::mapRecommendations)
        .onFailure((e) -> log.error(CANNOT_GET_RECOMMENDATIONS, e))
        .onSuccess(result -> log.debug("Recommendations were got successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(
                CANNOT_GET_RECOMMENDATIONS, e));
  }

  private Collection<RecommendationWithMetadataOut> mapRecommendations(
      Iterator<RecommendationWithMetadata> recommendationsInfo) {
    return Lists.newArrayList(recommendationsInfo).stream()
        .map(this::mapRecommendation)
        .collect(Collectors.toList());
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
