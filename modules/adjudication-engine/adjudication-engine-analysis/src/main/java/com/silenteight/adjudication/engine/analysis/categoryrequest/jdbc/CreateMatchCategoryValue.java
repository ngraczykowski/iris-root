package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryMap;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.categories.api.v1.CategoryValue;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
class CreateMatchCategoryValue {

  private final JdbcTemplate jdbcTemplate;

  int[] execute(CategoryMap categoryMap, List<CategoryValue> categoryValues) {
    return jdbcTemplate.batchUpdate(
        "INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)\n"
            + "VALUES (?, ?, now(), ?)",
        new CategoryValueParametrizedPreparedStatementSetter(categoryMap, categoryValues));
  }


  @RequiredArgsConstructor
  private static final class CategoryValueParametrizedPreparedStatementSetter
      implements BatchPreparedStatementSetter {

    private final CategoryMap categoryMap;
    private final List<CategoryValue> categoryValues;

    @SuppressWarnings("FeatureEnvy")
    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
      var categoryValue = categoryValues.get(i);
      var resourceName = ResourceName.create(categoryValue.getName());
      var matchId = resourceName.getLong("matches");
      var category = "categories/" + resourceName.get("categories");
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
      return categoryValues.size();
    }
  }
}
