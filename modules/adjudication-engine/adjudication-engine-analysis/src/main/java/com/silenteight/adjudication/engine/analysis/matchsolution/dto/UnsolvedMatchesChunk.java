package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.SolutionResponse;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Value
@Getter(AccessLevel.NONE)
public class UnsolvedMatchesChunk {

  long[] matchIds;

  List<FeatureVector> featureVectors;

  public UnsolvedMatchesChunk(long[] matchIds, @NonNull List<FeatureVector> featureVectors) {
    Preconditions.checkArgument(
        matchIds.length == featureVectors.size(), "Expected %s feature vectors, got %s",
        matchIds.length, featureVectors.size());
    Preconditions.checkArgument(!featureVectors.isEmpty(), "Feature vectors must not be empty");

    this.matchIds = Arrays.copyOf(matchIds, matchIds.length);
    this.featureVectors = featureVectors;
  }

  public BatchSolveFeaturesRequest toBatchSolveFeaturesRequest(
      String policy, FeatureCollection featureCollection) {

    var featureCount = featureCollection.getFeatureCount();
    var featureValueCount = featureVectors.get(0).getFeatureValueCount();
    Preconditions.checkArgument(
        featureCount == featureValueCount,
        "Feature collection has %s features, expected %s", featureCount, featureValueCount);

    return BatchSolveFeaturesRequest
        .newBuilder()
        .setPolicyName(policy)
        .setFeatureCollection(featureCollection)
        .addAllFeatureVectors(featureVectors)
        .build();
  }

  public MatchSolutionCollection toMatchSolutionCollection(
      long analysisId, List<SolutionResponse> solutionResponses) {

    Preconditions.checkArgument(
        matchIds.length == solutionResponses.size(),
        "Expected %s solution responses, got %s", matchIds.length, solutionResponses.size());

    var matchSolutions = IntStream
        .range(0, matchIds.length)
        .mapToObj(idx -> new MatchSolution(matchIds[idx], solutionResponses.get(idx)))
        .collect(toList());

    return new MatchSolutionCollection(analysisId, matchSolutions);
  }
}
