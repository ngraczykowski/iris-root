package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;


import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.MultiValue;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class JdbcMatchCategoryValuesDataAccessTest extends BaseDataJpaTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  @Autowired
  protected DataSource dataSource;

  @Test
  void shouldAddMissingCategoryForMatch() {
    var batchGetMatchesCategoryValuesResponse = BatchGetMatchesCategoryValuesResponse.newBuilder()
        .addCategoryValues(CategoryValue
            .newBuilder()
            .setName("categories/source_system/matches/1")
            .setMatch("matches/1")
            .setSingleValue("ASD")
            .build())
        .addCategoryValues(CategoryValue
            .newBuilder()
            .setName("categories/country/matches/1")
            .setMatch("categories/country/matches/1")
            .setMultiValue(MultiValue.newBuilder().addAllValues(List.of("PL", "SE")).build())
            .build()).build();
    var map = new CategoryMap(Map.of(
        "categories/source_system", 1L,
        "categories/country", 4L
    ));
    var inserts = new CreateMatchCategoryValue(jdbcTemplate)
        .execute(map, batchGetMatchesCategoryValuesResponse);

    assertThat(inserts[0]).isEqualTo(1);
    assertThat(inserts[1]).isEqualTo(1);
  }

  @Test
  void shouldSelectMissingCategories() {
    var missingCategoryResult = new SelectMissingMatchCategoryValuesQuery(
        new ObjectMapper(), jdbcTemplate, 4096).execute(1);

    assertThat(missingCategoryResult).isNotNull();
  }

}
