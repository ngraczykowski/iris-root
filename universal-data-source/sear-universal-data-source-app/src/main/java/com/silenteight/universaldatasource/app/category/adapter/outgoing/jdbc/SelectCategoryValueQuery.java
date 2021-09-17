package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.universaldatasource.app.category.model.MatchCategoryRequest;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
class SelectCategoryValueQuery {

  private final JdbcTemplate jdbcTemplate;

  List<CategoryValue> execute(List<MatchCategoryRequest> matchCategoryRequestList) {

    jdbcTemplate.execute("CREATE TEMP TABLE IF NOT EXISTS category_temp\n"
        + " (match_name VARCHAR NOT NULL, category_id VARCHAR NOT NULL)");

    jdbcTemplate.batchUpdate(
        "INSERT INTO category_temp VALUES(?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, matchCategoryRequestList.get(i).getMatch());
            ps.setString(2, matchCategoryRequestList.get(i).getCategory());
          }

          @Override
          public int getBatchSize() {
            return matchCategoryRequestList.size();
          }
        });

    String sql =
        "SELECT category_value_id, category_id, match_name, category_value\n"
            + " FROM uds_category_value\n"
            + " WHERE (match_name, category_id) IN (SELECT * FROM category_temp)";

    // FIXME(jgajewski): Rewrite it so it streams data out of the database, otherwise
    //  memory can easily run out (AEP-240).
    List<CategoryValue> categoryValues = jdbcTemplate.query(sql, new CategoryRowMapper());

    jdbcTemplate.execute("DROP TABLE IF EXISTS category_temp");

    return categoryValues;
  }

  private static final class CategoryRowMapper implements RowMapper<CategoryValue> {

    @Override
    public CategoryValue mapRow(ResultSet rs, int rowNum) throws SQLException {
      return CategoryValue
          .newBuilder()
          .setName(rs.getString(2) + "/value/" + rs.getLong(1))
          .setMatch(rs.getString(3))
          .setSingleValue(rs.getString(4))
          .build();
    }
  }
}
