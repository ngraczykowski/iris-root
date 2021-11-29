package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.universaldatasource.common.resource.AlertName.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = {
    "populate_categories.sql",
    "populate_category_values.sql"
})
@Import({
    JdbcCategoryValueDataAccess.class,
    JdbcCategoryDataAccess.class,
    JdbcCategoryConfiguration.class,
    DeleteCategoryValueQuery.class,
    InsertCategoryValueQuery.class,
    SelectCategoryValueQuery.class
})
class JdbcCategoryValueDataAccessIT extends BaseJdbcTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  @Autowired
  private JdbcCategoryValueDataAccess categoryValueDataAccess;

  @Test
  void shouldDeleteCategoryValues() {

    var alerts = getListOfAlerts(List.of(1, 2, 3, 4, 5));

    assertEquals(5, categoryValueDataAccess.delete(alerts));
    assertEquals(5, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM uds_category_value", Integer.class));
  }
}
