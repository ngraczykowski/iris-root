package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
@Slf4j
class CreateMatchCategoryValue {

  private final JdbcTemplate jdbcTemplate;

  int[] execute(CategoryMap categoryMap, BatchGetMatchesCategoryValuesResponse categoryValues) {
    return jdbcTemplate.batchUpdate(
        "INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)\n"
            + "VALUES (?, ?, now(), ?)\n"
            + "ON CONFLICT DO NOTHING",
        new CategoryValueParametrizedPreparedStatementSetter(categoryMap, categoryValues));
  }

  @RequiredArgsConstructor
  private static final class CategoryValueParametrizedPreparedStatementSetter
      implements BatchPreparedStatementSetter {

    private final CategoryMap categoryMap;
    private final BatchGetMatchesCategoryValuesResponse categoryValues;

    @SuppressWarnings("FeatureEnvy")
    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
      var categoryValue = categoryValues.getCategoryValuesList().get(i);
      var resourceCategory = ResourceName.create(categoryValue.getName());
      var resourceMatch = ResourceName.create(categoryValue.getMatch());
      var matchId = resourceMatch.getLong("matches");
      var category = "categories/" + resourceCategory.get("categories");
      var categoryId = categoryMap.getCategoryId(category);

      ps.setLong(1, matchId);
      ps.setLong(2, categoryId);

      if (categoryValue.hasMultiValue()) {
        // XXX(ahaczewski): How should we treat multiple values in category?
        ps.setString(3, String.join(",", categoryValue.getMultiValue().getValuesList()));
      } else {
        ps.setString(3, categoryValue.getSingleValue());
      }
    }

    @Override
    public int getBatchSize() {
      return categoryValues.getCategoryValuesList().size();
    }
  }
}
