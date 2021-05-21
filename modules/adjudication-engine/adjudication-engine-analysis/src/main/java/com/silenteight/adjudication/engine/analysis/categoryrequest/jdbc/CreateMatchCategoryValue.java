package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.categories.api.v1.CategoryValue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
class CreateMatchCategoryValue {

  private final CategoryRequestJdbcConfiguration configuration;
  private final JdbcTemplate jdbcTemplate;

  int[][] execute(
      @NonNull List<CategoryValue> missingCategoryValues, @NonNull Map<String, Long> categories) {
    return jdbcTemplate.batchUpdate(
        "INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)"
            + " VALUES(?, ?, NOW(), ?)",
        missingCategoryValues,
        configuration.getInsertBatchSize(),
        new CategoryValueParametrizedPreparedStatementSetter(categories));
  }


  private static final class CategoryValueParametrizedPreparedStatementSetter
      implements ParameterizedPreparedStatementSetter<CategoryValue> {

    private final Map<String, Long> categories;

    CategoryValueParametrizedPreparedStatementSetter(
        @NonNull Map<String, Long> categories) {
      this.categories = categories;
    }

    @Override
    public void setValues(PreparedStatement ps, CategoryValue argument) throws SQLException {
      var resourceName = ResourceName.create(argument.getName());
      var matchId = resourceName.getLong("matches");
      var categoryKey = "categories/" + resourceName.get("categories");

      if (!categories.containsKey(categoryKey)) {
        throw new CategoryNotFoundInLocalStorageMap(categoryKey);
      }

      ps.setLong(1, matchId);
      ps.setLong(2, categories.get(categoryKey));
      if (argument.hasMultiValue()) {
        ps.setString(3, argument.getMultiValue().getValuesList()
            .stream()
            .collect(Collectors.joining(",")));
      } else {
        ps.setString(3, argument.getSingleValue());
      }
    }
  }

}
