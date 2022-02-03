package com.silenteight.warehouse.report.generation;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.warehouse.report.generation.QueryParamsSubstitutor.PARAMETER_COUNTRIES;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class QueryParamsSubstitutorTest {

  private static final String FILENAME = "file.csv";
  private static final String SELECT_SQL_QUERY = "SELECT *";

  @Test
  void shouldHandleDataAccessPermissionParameter() {
    ReportRequestData request = ReportRequestData.builder()
        .fileStorageName(FILENAME)
        .selectSqlQuery(SELECT_SQL_QUERY)
        .sqlTemplates(
            of("SELECT * FROM table WHERE countries IN (${" + PARAMETER_COUNTRIES + "})"))
        .dataAccessPermissions(orderedSet("PL", "UK"))
        .build();

    SqlExecutorDto result = QueryParamsSubstitutor.substitute(request);

    assertThat(result.getPrepareDataSqlStatements())
        .containsExactly("SELECT * FROM table WHERE countries IN ('PL', 'UK')");
  }

  @Test
  void shouldHandleEmptyParams() {
    ReportRequestData request = ReportRequestData.builder()
        .fileStorageName(FILENAME)
        .selectSqlQuery(SELECT_SQL_QUERY)
        .sqlTemplates(List.of())
        .build();

    SqlExecutorDto result = QueryParamsSubstitutor.substitute(request);

    assertThat(result.getPrepareDataSqlStatements()).isEmpty();
    assertThat(result.getSelectSqlStatement()).isEqualTo(SELECT_SQL_QUERY);
  }

  private Set<String> orderedSet(String... elements) {
    return new LinkedHashSet<>(asList(elements));
  }
}
