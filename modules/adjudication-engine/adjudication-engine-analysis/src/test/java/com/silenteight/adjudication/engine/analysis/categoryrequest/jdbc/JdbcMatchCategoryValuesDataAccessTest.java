package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;


import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryMap;
import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.datasource.categories.api.v1.CategoryValue;
import com.silenteight.datasource.categories.api.v1.MultiValue;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class JdbcMatchCategoryValuesDataAccessTest extends BaseDataJpaTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  private CategoryRequestJdbcConfiguration configuration;

  @BeforeEach
  public void setUp() {
    configuration = new CategoryRequestJdbcConfiguration(1024, 4096);
  }

  @Test
  void shouldAddMissingCategoryForMatch() {
    var sourceSystemCategory = CategoryValue.newBuilder()
        .setSingleValue("ECDD")
        .setName("categories/source_system/matches/1")
        .build();
    var countryCategory = CategoryValue.newBuilder()
        .setMultiValue(MultiValue.newBuilder().addValues("PL").addValues("SE").build())
        .setName("categories/country/matches/1")
        .build();
    var map = new CategoryMap(Map.of(
        "categories/source_system", 1L,
        "categories/country", 4L
    ));
    var inserts = new CreateMatchCategoryValue(jdbcTemplate)
        .execute(map, List.of(countryCategory, sourceSystemCategory));

    assertThat(inserts[0]).isEqualTo(1);
    assertThat(inserts[1]).isEqualTo(1);
  }

  @Test
  void shouldSelectMissingCategories() {
    var missingCategoryResult
        = new SelectMissingMatchCategoryValuesQuery(configuration, jdbcTemplate)
        .execute(1);
    assertThat(missingCategoryResult).isNotNull();
  }

}
