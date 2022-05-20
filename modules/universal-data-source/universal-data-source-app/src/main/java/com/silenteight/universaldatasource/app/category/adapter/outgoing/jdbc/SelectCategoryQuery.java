package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
class SelectCategoryQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT category_id, category_display_name, category_type,\n"
          + " allowed_values, multi_value FROM uds_category";

  private final JdbcTemplate jdbcTemplate;

  List<Category> execute() {
    return jdbcTemplate.query(SQL, new CategoryRowMapper());
  }

  private static final class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Category.newBuilder()
          .setName(rs.getString(1))
          .setDisplayName(rs.getString(2))
          .setType(CategoryType.valueOf(rs.getString(3)))
          .addAllAllowedValues(splitAllowedValues(rs.getString(4)))
          .setMultiValue(rs.getBoolean(5))
          .build();
    }

    private static List<String> splitAllowedValues(String allowedValues) {
      return Arrays.asList(allowedValues.split(","));
    }
  }
}
