package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import com.silenteight.solving.api.v1.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureCollection;
import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureVector;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.util.stream.Collectors.toList;
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
        .hasMessageContainingAll("Feature collection has", "features, expected");
  }

  @Test
  void shouldCreateRequest() {
    givenMatchIds(1L);
    givenFeatureVectors(makeFeatureVector("V1", "V2"));

    var request = whenBatchSolveFeaturesRequest("F1", "F2");

    assertThat(request.getPolicyName()).isEqualTo(POLICY);
    assertThat(request.getFeatureVectorsList()).containsAll(featureVectors);
    assertThat(request.getFeatureCollection().getFeatureList())
        .extracting(Feature::getName)
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
        .mapToObj(idx -> new UnsolvedMatch(
            ALERT_ID, matchIds[idx], new String[0],
            featureVectors.get(idx).getFeatureValueList().toArray(String[]::new)))
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
}
