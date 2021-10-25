package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.Category;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.Feature;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.SolutionResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeCategory;
import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeature;
import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureCollection;
import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeFeatureVector;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcUnsolvedMatchesReaderConfiguration.class,
})
@Sql
@ExtendWith(MockitoExtension.class)
@Slf4j
@TestPropertySource(properties = {
    "ae.analysis.match-solution.unsolved-matches-reader.chunkSize=2",
    "ae.analysis.match-solution.unsolved-matches-reader.maxRows=5",
})
class JdbcUnsolvedMatchesReaderIT extends BaseJdbcTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  JdbcUnsolvedMatchesReader reader;

  @Mock
  UnsolvedMatchesReader.ChunkHandler chunkHandler;

  @Captor
  ArgumentCaptor<UnsolvedMatchesChunk> unsolvedMatchesChunkArgumentCaptor;

  @Test
  void readsNothingForInvalidAnalysis() {
    var readMatches = reader.readInChunks(123, chunkHandler);

    assertThat(readMatches).isZero();

    verify(chunkHandler).finished();
  }

  @Test
  void readsOnlyMatchWithAllCategoriesAndFeatures() {
    var readMatches = reader.readInChunks(1, chunkHandler);

    assertThat(readMatches).isOne();

    verify(chunkHandler).handle(unsolvedMatchesChunkArgumentCaptor.capture());
    verify(chunkHandler).finished();

    assertThat(unsolvedMatchesChunkArgumentCaptor.getAllValues()).hasSize(1);

    var unsolvedMatchesChunk = unsolvedMatchesChunkArgumentCaptor.getValue();

    var request =
        unsolvedMatchesChunk.toBatchSolveFeaturesRequest("policies/dummy", featureCollection());
    var solutions =
        unsolvedMatchesChunk.toMatchSolutionCollection(1L, List.of(solutionResponse()));

    assertThat(request.getFeatureVectorsList())
        .hasSize(1)
        .containsExactly(featureVector());

    assertThat(solutions.getMatchSolutions())
        .hasSize(1)
        .containsExactly(
            MatchSolution
                .builder()
                .alertId(1)
                .matchId(11)
                .clientMatchIdentifier("match-11")
                .response(solutionResponse())
                .categories(categories())
                .features(features())
                .build());
  }

  @Test
  void readsAllRecordsInMultipleBatches() {
    var analysisId = 2;
    mockInsertSolutionOnReceive(analysisId);

    var readMatches = reader.readInChunks(analysisId, chunkHandler);

    assertThat(readMatches).isEqualTo(11);

    verify(chunkHandler, times(7)).handle(unsolvedMatchesChunkArgumentCaptor.capture());
    verify(chunkHandler, times(3)).finished();
  }

  @Test
  void shouldReadRecordsForAnalysisWithoutAnyCategories() {
    var analysisId = 3;
    mockInsertSolutionOnReceive(analysisId);

    var readMatches = reader.readInChunks(analysisId, chunkHandler);

    assertThat(readMatches).isEqualTo(11);
  }

  @Test
  void shouldReadRecordsForAnalysisWithoutAnyFeatures() {
    var analysisId = 4;
    mockInsertSolutionOnReceive(analysisId);

    var readMatches = reader.readInChunks(analysisId, chunkHandler);

    assertThat(readMatches).isEqualTo(11);
  }

  private void mockInsertSolutionOnReceive(int analysisId) {
    doAnswer((Answer<Object>) invocation -> {
      insertSolutionForChunk(analysisId, invocation.getArgument(0, UnsolvedMatchesChunk.class));
      return null;
    }).when(chunkHandler).handle(any());
  }

  private void insertSolutionForChunk(long analysisId, UnsolvedMatchesChunk chunk) {
    chunk.getMatchNames().stream()
        .map(n -> ResourceName.create(n).getLong("matches"))
        .forEach(id -> jdbcTemplate.execute(""
            + "INSERT INTO ae_match_solution"
            + " (analysis_id, match_id, created_at, solution, match_context) "
            + "VALUES "
            + "(" + analysisId + ", " + id + ", now(), 'solution', '{}')"));
  }

  private static FeatureVector featureVector() {
    return makeFeatureVector("DENY", "C", "NID_NO_MATCH", "DOC_MATCH", "NAM_EXACT_MATCH");
  }

  private static FeatureCollection featureCollection() {
    return makeFeatureCollection(
        "categories/hit_type", "categories/customer_type", "features/national_id",
        "features/passport", "features/name");
  }

  private static SolutionResponse solutionResponse() {
    return SolutionResponse
        .newBuilder()
        .setFeatureVectorSolution(SOLUTION_FALSE_POSITIVE)
        .build();
  }

  private static List<Category> categories() {
    return List.of(
        makeCategory("categories/hit_type", "DENY"),
        makeCategory("categories/customer_type", "C"));
  }

  private static List<Feature> features() {
    return List.of(
        makeFeature(
            "features/national_id",
            "NID_NO_MATCH",
            "agents/document/versions/2.1.0/configs/1"),
        makeFeature(
            "features/passport",
            "DOC_MATCH",
            "agents/document/versions/2.1.0/configs/1"),
        makeFeature(
            "features/name",
            "NAM_EXACT_MATCH",
            "agents/name/versions/3.3.0/configs/1"));
  }
}
