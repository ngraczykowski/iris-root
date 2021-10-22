package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.SolutionResponse;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureCollection;
import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureVector;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.*;

class UnsolvedMatchesChunkTest {

  private static final String POLICY = "policy";
  private static final long ANALYSIS_ID = 123L;
  private static final long ALERT_ID = 456L;

  private long[] matchIds;
  private List<FeatureVector> featureVectors;
  private UnsolvedMatchesChunk chunk;

  @Test
  void shouldFailOnNoFeatureVectors() {
    givenMatchIds();
    givenFeatureVectors();

    assertThatThrownBy(this::whenNewChunk)
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldFailForDifferentNumberOfFeaturesThanValues() {
    givenMatchIds(1L);
    givenFeatureVectors(makeFeatureVector("V1", "V2"));

    assertThatThrownBy(() -> whenBatchSolveFeaturesRequest("F1", "F2", "F3"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContainingAll("Matches have", "features, feature collection expected");
  }

  @Test
  void shouldCreateRequest() {
    givenMatchIds(1L);
    givenFeatureVectors(makeFeatureVector("V1", "V2"));

    var request = whenBatchSolveFeaturesRequest("F1", "F2");

    assertThat(request.getPolicyName()).isEqualTo(POLICY);
    assertThat(request.getFeatureVectorsList()).containsAll(featureVectors);
    assertThat(request.getFeatureCollection().getFeatureList())
        .extracting(com.silenteight.solving.api.v1.Feature::getName)
        .containsExactly("F1", "F2");
  }

  @Test
  void shouldFailForUnexpectedSolutions() {
    givenMatchIds(1L, 2L);
    givenFeatureVectors(makeFeatureVector("V1"), makeFeatureVector("V2"));

    assertThatThrownBy(() -> whenMatchSolutionCollection(SOLUTION_NO_DECISION))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldCreateSolutionCollection() {
    givenMatchIds(1L, 2L);
    givenFeatureVectors(makeFeatureVector("M1V1"), makeFeatureVector("M2V1"));

    var solutionCollection =
        whenMatchSolutionCollection(SOLUTION_FALSE_POSITIVE, SOLUTION_NO_DECISION);

    assertThat(solutionCollection.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(solutionCollection.getMatchSolutions())
        .extracting(MatchSolution::getMatchId)
        .containsExactly(1L, 2L);
    assertThat(solutionCollection.getMatchSolutions())
        .extracting(MatchSolution::getResponse)
        .extracting(SolutionResponse::getFeatureVectorSolution)
        .containsExactly(SOLUTION_FALSE_POSITIVE, SOLUTION_NO_DECISION);
  }

  private void givenMatchIds(long... matchIds) {
    this.matchIds = matchIds;
  }

  private void givenFeatureVectors(FeatureVector... featureVectors) {
    this.featureVectors = List.of(featureVectors);
  }

  private UnsolvedMatchesChunk whenNewChunk() {
    var unsolvedMatches = IntStream
        .range(0, matchIds.length)
        .mapToObj(idx ->
            UnsolvedMatch
                .builder()
                .alertId(ALERT_ID)
                .matchId(matchIds[idx])
                .categories(emptyList())
                .features(toFeatures(featureVectors.get(idx)))
                .build())
        .collect(toList());

    chunk = new UnsolvedMatchesChunk(unsolvedMatches);
    return chunk;
  }

  private BatchSolveFeaturesRequest whenBatchSolveFeaturesRequest(String... features) {
    return whenNewChunk().toBatchSolveFeaturesRequest(
        POLICY, makeFeatureCollection(features));
  }

  private MatchSolutionCollection whenMatchSolutionCollection(FeatureVectorSolution... solutions) {
    var solutionResponses = Arrays.stream(solutions)
        .map(s -> SolutionResponse.newBuilder().setFeatureVectorSolution(s).build())
        .collect(toList());

    return whenNewChunk().toMatchSolutionCollection(ANALYSIS_ID, solutionResponses);
  }

  private static List<Feature> toFeatures(
      FeatureVector vector) {
    return IntStream.range(0, vector.getFeatureValueCount())
        .mapToObj(idx -> Feature
            .builder()
            .name("feature" + idx)
            .agentConfig("config" + idx)
            .value(vector.getFeatureValue(idx))
            .build())
        .collect(toUnmodifiableList());
  }
}
