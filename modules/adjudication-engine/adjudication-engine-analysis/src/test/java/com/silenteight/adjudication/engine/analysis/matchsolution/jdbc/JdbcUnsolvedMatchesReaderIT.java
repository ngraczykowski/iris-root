package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
class JdbcUnsolvedMatchesReaderIT extends BaseJdbcTest {

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
        .containsExactly(new MatchSolution(1L, 11L, solutionResponse()));
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
}
