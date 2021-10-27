package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;


import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
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
    var sourceSystemCategory = GetCategoryValueResponse.builder()
        .singleValue("ECDD")
        .categoryName("categories/source_system/matches/1")
        .matchName("matches/1")
        .build();
    var countryCategory = GetCategoryValueResponse.builder()
        .multiValues(List.of("PL", "SE"))
        .multiValue(true)
        .categoryName("categories/country/matches/1")
        .matchName("categories/country/matches/1")
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
    var missingCategoryResult =
        new SelectMissingMatchCategoryValuesQuery(new ObjectMapper(), dataSource, 4096).execute(1);

    assertThat(missingCategoryResult).isNotNull();
  }

}
