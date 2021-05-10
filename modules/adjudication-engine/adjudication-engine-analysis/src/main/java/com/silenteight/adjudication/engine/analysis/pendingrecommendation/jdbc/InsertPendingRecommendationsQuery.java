package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.util.ResourceUtils;

import java.util.ResourceBundle;

@RequiredArgsConstructor
class InsertPendingRecommendationsQuery {

  private final SqlUpdate query;

  InsertPendingRecommendationsQuery(JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
    query = new SqlUpdate();

    query.setJdbcTemplate(jdbcTemplate);
    query.setSql(resourceLoader.getResource("classpath:" + getClass().getName() + ".sql").);
  }

  int execute(long analysisId) {
    jdbcTemplate.execute((PreparedStatementCreator) con -> con.prepareStatement(query), )
  }

}
