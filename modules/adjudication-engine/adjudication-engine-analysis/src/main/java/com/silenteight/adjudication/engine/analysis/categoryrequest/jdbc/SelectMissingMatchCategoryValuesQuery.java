package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MatchAlert;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingMatchCategory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        "SELECT category, category_id, match_alert\n"
            + "FROM ae_missing_match_category_values_query\n"
            + "WHERE analysis_id = ?",
        new SqlMissingMatchCategoryExtractor(),
        analysisId);
  }

  private static final class SqlMissingMatchCategoryExtractor implements
      ResultSetExtractor<MissingCategoryResult> {

    @SuppressWarnings("InnerClassTooDeeplyNested")
    @Override
    public MissingCategoryResult extractData(ResultSet rs) throws SQLException {
      var missingMatchCategories = new ArrayList<MissingMatchCategory>();
      var categories = new HashMap<String, Long>();
      ObjectMapper objectMapper = new ObjectMapper();

      while (rs.next()) {
        var category = rs.getString("category");
        var categoryId = rs.getLong("category_id");

        try {
          var matchAlerts = objectMapper.readValue(
              rs.getString("match_alert"), new TypeReference<List<MatchAlert>>() {});
          missingMatchCategories.add(
              MissingMatchCategory.builder().categoryName(category).matches(matchAlerts).build());
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Couldn't map match alert", e);
        }

        categories.putIfAbsent(category, categoryId);
      }

      return new MissingCategoryResult(missingMatchCategories, new CategoryMap(categories));
    }
  }
}
