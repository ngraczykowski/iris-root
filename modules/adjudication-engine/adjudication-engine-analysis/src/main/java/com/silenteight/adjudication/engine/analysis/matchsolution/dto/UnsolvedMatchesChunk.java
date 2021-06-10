package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.SolutionResponse;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@EqualsAndHashCode
@ToString
public class UnsolvedMatchesChunk {

  private final List<UnsolvedMatch> matches;

  public UnsolvedMatchesChunk(@NonNull List<? extends UnsolvedMatch> matches) {
    Preconditions.checkArgument(!matches.isEmpty(), "Matches must not be empty");

    this.matches = unmodifiableList(matches);
  }

  public List<String> getMatchNames() {
    return matches.stream().map(UnsolvedMatch::getMatchName).collect(toUnmodifiableList());
  }

  public BatchSolveFeaturesRequest toBatchSolveFeaturesRequest(
      String policy, FeatureCollection featureCollection) {

    var featureCount = featureCollection.getFeatureCount();
    var featureValueCount = matches.get(0).getFeatureValueCount();
    Preconditions.checkArgument(
        featureCount == featureValueCount,
        "Feature collection has %s features, expected %s", featureCount, featureValueCount);

    return BatchSolveFeaturesRequest
        .newBuilder()
        .setPolicyName(policy)
        .setFeatureCollection(featureCollection)
        .addAllFeatureVectors(collectFeatureVectors())
        .build();
  }

  private Collection<FeatureVector> collectFeatureVectors() {
    return matches.stream().map(UnsolvedMatch::toFeatureVector).collect(toUnmodifiableList());
  }

  public MatchSolutionCollection toMatchSolutionCollection(
      long analysisId, List<SolutionResponse> solutionResponses) {

    Preconditions.checkArgument(
        matches.size() == solutionResponses.size(),
        "Expected %s solution responses, got %s", matches.size(), solutionResponses.size());

    var matchSolutions = IntStream
        .range(0, matches.size())
        .mapToObj(idx -> matches.get(idx).toMatchSolution(solutionResponses.get(idx)))
        .collect(toList());

    return new MatchSolutionCollection(analysisId, matchSolutions);
  }
}
