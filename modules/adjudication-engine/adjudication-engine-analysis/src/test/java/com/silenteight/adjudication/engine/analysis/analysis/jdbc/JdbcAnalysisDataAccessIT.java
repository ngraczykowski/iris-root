package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisDataAccess;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcAnalysisDataAccess.class,
    SelectAnalysisAgentConfigQuery.class,
    SelectAnalysisByPendingRecommendationMatches.class,
    SelectFeatureVectorElementsQuery.class,
    SelectAnalysisAttachmentFlagsQuery.class
})
@Sql
class JdbcAnalysisDataAccessIT extends BaseJdbcTest {

  @Autowired
  AnalysisDataAccess jdbcAnalysisDataAccess;

  @Test
  void shouldGetPolicy() {
    var policy = jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(1).getPolicy();
    assertThat(policy).isEqualTo("policies/asd");
  }

  @Test
  void shouldReturnListOfFutureAndCategories() {
    var names = List.of("categories/country", "categories/ser", "features/passport");
    var result = jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(1).toFeatureCollection();

    assertThat(result.getFeatureList())
        .isNotEmpty()
        .allSatisfy(f -> assertThat(names).contains(f.getName()));
  }

  @Test
  void shouldThrowExceptionWhenInvalidId() {
    assertThrows(
        org.springframework.dao.EmptyResultDataAccessException.class,
        () -> jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(420));
  }

  @Test
  void shouldGetAllAnalysisId() {
    var matchIds = List.of(11L, 12L);

    assertThat(jdbcAnalysisDataAccess.findByPendingRecommendationMatchIds(matchIds))
        .isEqualTo(List.of(1L, 2L));
  }
}
