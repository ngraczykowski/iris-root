package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MatchAlert;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingMatchCategory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
class SelectMissingMatchCategoryValuesQuery {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;
  private final int batchSize;
  private CollectionType matchAlertListType;

  SelectMissingMatchCategoryValuesQuery(
      ObjectMapper objectMapper, DataSource datasource, int batchSize) {

    this.objectMapper = objectMapper;
    this.batchSize = batchSize;

    matchAlertListType = this.objectMapper
        .getTypeFactory()
        .constructCollectionType(ArrayList.class, MatchAlert.class);

    jdbcTemplate = new JdbcTemplate(datasource, true);
    jdbcTemplate.setMaxRows(this.batchSize);
  }

  MissingCategoryResult execute(long analysisId) {
    return jdbcTemplate.query(
        "SELECT category, category_id, match_alert\n"
            + "FROM ae_missing_match_category_values_query\n"
            + "WHERE analysis_id = ?\n"
            + "LIMIT ?",
        new SqlMissingMatchCategoryExtractor(),
        analysisId, batchSize);
  }

  private final class SqlMissingMatchCategoryExtractor implements
      ResultSetExtractor<MissingCategoryResult> {

    @SuppressWarnings("InnerClassTooDeeplyNested")
    @Override
    public MissingCategoryResult extractData(ResultSet rs) throws SQLException {
      var missingMatchCategories = new ArrayList<MissingMatchCategory>();
      var categories = new HashMap<String, Long>();

      while (rs.next()) {
        var category = rs.getString("category");
        var categoryId = rs.getLong("category_id");

        var matchAlerts = deserializeMatchAlerts(rs);
        missingMatchCategories.add(
            MissingMatchCategory.builder().categoryName(category).matches(matchAlerts).build());
        categories.putIfAbsent(category, categoryId);
      }

      return new MissingCategoryResult(missingMatchCategories, new CategoryMap(categories));
    }

    private List<MatchAlert> deserializeMatchAlerts(ResultSet rs) throws SQLException {
      try {
        return objectMapper.readValue(rs.getString("match_alert"), matchAlertListType);
      } catch (JsonProcessingException e) {
        return rethrow(e);
      }
    }
  }
}
