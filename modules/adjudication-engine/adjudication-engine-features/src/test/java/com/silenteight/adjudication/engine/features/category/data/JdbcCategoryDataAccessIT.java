package com.silenteight.adjudication.engine.features.category.data;

import com.silenteight.adjudication.engine.features.category.CategoryDataAccess;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    CategoryDataAccessConfiguration.class,
    JdbcCategoryDataAccess.class,
})
class JdbcCategoryDataAccessIT extends BaseJdbcTest {

  private static final String CATEGORIES_SOURCE_SYSTEM = "categories/sourceSystem";
  protected static final String CATEGORIES_COUNTRY = "categories/country";

  @Autowired
  CategoryDataAccess dataAccess;

  @Test
  void insertsNewCategories() {
    var affected = dataAccess.addCategories(List.of(CATEGORIES_SOURCE_SYSTEM, CATEGORIES_COUNTRY));
    var count = countCategories();

    assertThat(count).isEqualTo(affected).isEqualTo(2);
  }

  @Test
  void duplicatesAreNotInserted() {
    dataAccess.addCategories(List.of(CATEGORIES_SOURCE_SYSTEM, CATEGORIES_COUNTRY));
    var affected = dataAccess.addCategories(List.of(CATEGORIES_COUNTRY));

    assertThat(affected).isZero();
  }

  @Nullable
  private Integer countCategories() {
    return jdbcTemplate.queryForObject("SELECT count(1) FROM ae_category", Integer.class);
  }
}
