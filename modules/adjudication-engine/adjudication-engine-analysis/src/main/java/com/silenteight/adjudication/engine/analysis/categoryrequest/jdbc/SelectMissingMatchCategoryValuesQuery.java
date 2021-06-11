package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.MissingCategoryResult;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.DataSource;

@Slf4j
class SelectMissingMatchCategoryValuesQuery {

  private final JdbcTemplate jdbcTemplate;

  SelectMissingMatchCategoryValuesQuery(DataSource datasource, int batchSize) {
    jdbcTemplate = new JdbcTemplate(datasource, true);
    jdbcTemplate.setMaxRows(batchSize);
  }

  MissingCategoryResult execute(long analysisId) {
    return jdbcTemplate.query(
        "SELECT category, category_id, match_id, alert_id\n"
            + "FROM ae_missing_match_category_values_query\n"
            + "WHERE analysis_id = ?",
        new SqlMissingMatchCategoryExtractor(),
        analysisId);
  }

  private static final class SqlMissingMatchCategoryExtractor implements
      ResultSetExtractor<MissingCategoryResult> {

    @Override
    public MissingCategoryResult extractData(ResultSet rs) throws SQLException {
      var missingMatchCategories = new ArrayList<String>();
      var categories = new HashMap<String, Long>();

      while (rs.next()) {
        var category = rs.getString(1);
        var categoryId = rs.getLong(2);
        var matchId = rs.getInt(3);
        var alertId = rs.getInt(4);

        var matchCategoryValueName = category + "/alerts/" + alertId + "/matches/" + matchId;

        missingMatchCategories.add(matchCategoryValueName);
        categories.putIfAbsent(category, categoryId);
      }

      return new MissingCategoryResult(missingMatchCategories, new CategoryMap(categories));
    }
  }
}
