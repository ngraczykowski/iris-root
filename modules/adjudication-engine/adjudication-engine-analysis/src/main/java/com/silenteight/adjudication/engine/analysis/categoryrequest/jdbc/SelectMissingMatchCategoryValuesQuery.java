package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.MissingCategoryResult;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
@Component
@Slf4j
class SelectMissingMatchCategoryValuesQuery {

  private final CategoryRequestJdbcConfiguration configuration;
  private final JdbcTemplate jdbcTemplate;


  MissingCategoryResult execute(long analysisId) {
    this.jdbcTemplate.setMaxRows(configuration.getSelectBatchSize());
    String sql =
        "SELECT category,category_id,match_id,alert_id "
            + "FROM ae_missing_match_category_values_query a "
            + "WHERE a.analysis_id = ?";
    return jdbcTemplate.query(
        sql, new Object[] { analysisId }, new SqlMissingMatchCategoryExtractor());
  }

  static class SqlMissingMatchCategoryExtractor implements
      ResultSetExtractor<MissingCategoryResult> {

    @Override
    public MissingCategoryResult extractData(ResultSet rs) throws SQLException {
      MissingCategoryResult result = new MissingCategoryResult(new ArrayList<>(), new HashMap<>());

      while (rs.next()) {
        var category = rs.getString("category");
        var categoryId = rs.getLong("category_id");
        var matchId = rs.getInt("match_id");
        var alertId = rs.getInt("alert_id");
        var resultResource = category + "/alerts/" + alertId + "/matches/" + matchId;

        result.addMissingMatchCategory(resultResource);
        result.addCategoryMapping(category, categoryId);
      }
      log.debug("Missing match category:{}", result);
      return result;
    }
  }
}
