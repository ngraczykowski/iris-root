/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class SelectAnalysisMatchCategoriesQuery {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @Language("PostgreSQL")
  private static final String QUERY = """
         SELECT aa.analysis_id, aaa.alert_id, am.match_id, ac.category, amcv.value
         FROM ae_analysis aa
                  JOIN ae_analysis_alert aaa ON aaa.analysis_id = aa.analysis_id
                  JOIN ae_match am ON aaa.alert_id = am.alert_id
                  JOIN ae_analysis_category aac ON aac.analysis_id = aa.analysis_id
                  JOIN ae_category ac ON ac.category_id = aac.category_id
                  LEFT JOIN ae_match_category_value amcv ON am.match_id = amcv.match_id
                                                            AND amcv.category_id = ac.category_id
         WHERE aa.analysis_id = :analysis
            AND aaa.alert_id = :alert
         """;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Map<Long, List<MatchCategoryDao>> findAlertMatchCategories(Long analysis, Long alert) {
    var parameters = new MapSqlParameterSource(Map.of(
        "analysis", analysis,
        "alert", alert
    ));
    var rowMapper = new MatchCategoriesRowMapper();
    jdbcTemplate.query(QUERY, parameters, rowMapper);
    return rowMapper.getCategories();
  }

  @Getter
  private static class MatchCategoriesRowMapper
      implements RowMapper<Long> {

    private final Map<Long, List<MatchCategoryDao>> categories = new HashMap<>();

    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
      var matchId = rs.getLong("match_id");
      var category = rs.getString("category");
      var categoryValue = rs.getString("value");

      var categoryDao = categories.getOrDefault(
          matchId,new ArrayList<>());

      categoryDao.add(new MatchCategoryDao(matchId,category,categoryValue));
      categories.putIfAbsent(matchId, categoryDao);
      return matchId;
    }
  }
}
