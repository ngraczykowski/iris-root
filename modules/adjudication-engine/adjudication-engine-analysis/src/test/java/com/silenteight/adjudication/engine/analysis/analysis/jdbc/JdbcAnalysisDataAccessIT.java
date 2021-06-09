package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisDataAccess;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcAnalysisDataAccess.class,
    SelectAnalysisAgentConfigQuery.class,
    SelectFeatureVectorElementsQuery.class
})
@Sql
class JdbcAnalysisDataAccessIT extends BaseJdbcTest {

  @Autowired
  AnalysisDataAccess jdbcAnalysisDataAccess;

  @Test
  void shouldGetPolicy() {
    var policy = jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(1).getPolicy();
    assertThat(policy).isEqualTo("policies/1");
  }

  @Test
  void shouldReturnListOfFutureAndCategories() {
    var names = List.of("categories/country", "categories/ser", "features/passport");
    var result = jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(1).toFeatureCollection();

    assertThat(result.getFeatureList())
        .allSatisfy(f -> assertThat(names).contains(f.getName()));
  }

  @Test
  void shouldThrowExceptionWhenInvalidId() {
    assertThrows(
        org.springframework.dao.EmptyResultDataAccessException.class,
        () -> jdbcAnalysisDataAccess.getPolicyAndFeatureVectorElements(420));
  }
}
