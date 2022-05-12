package com.silenteight.warehouse.report.generation;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.warehouse.report.generation.QueryParamsSubstitutor.PARAMETER_COUNTRIES;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class QueryParamsSubstitutorTest {

  private static final String NAME = "AI_REASONING";
  private static final String FILENAME = "file.csv";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now();
  private static final String SELECT_SQL_QUERY = "SELECT *";
  private static final Long DOMAIN_ID = 1L;

  @Test
  void shouldHandleDataAccessPermissionParameter() {
    ReportRequestData request = ReportRequestData.builder()
        .name(NAME)
        .fileStorageName(FILENAME)
        .selectSqlQuery(SELECT_SQL_QUERY)
        .createdAt(CREATED_AT)
        .domainId(DOMAIN_ID)
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
        .name(NAME)
        .fileStorageName(FILENAME)
        .selectSqlQuery(SELECT_SQL_QUERY)
        .createdAt(CREATED_AT)
        .domainId(DOMAIN_ID)
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
