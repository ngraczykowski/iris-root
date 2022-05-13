package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static com.silenteight.universaldatasource.common.resource.AlertName.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("populate_categories.sql")
@Sql(scripts = "truncate_categories.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    var alerts = getListOfAlerts(5, 6, 7, 8, 9);

    assertEquals(5, categoryValueDataAccess.delete(alerts));
    assertEquals(10, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM uds_category_value", Integer.class));
  }
}
