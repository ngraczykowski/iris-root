package com.silenteight.warehouse.report.generation;

import lombok.experimental.UtilityClass;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

import java.util.*;

import static com.silenteight.warehouse.common.time.Timestamps.toStringFormatIsoLocalDate;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.text.StringSubstitutor.replace;

@UtilityClass
class QueryParamsSubstitutor {

  static final String PARAMETER_FROM = "from";
  static final String PARAMETER_TO = "to";
  static final String PARAMETER_ANALYSIS_ID = "analysisId";
  static final String PARAMETER_COUNTRIES = "countries";
  static final String MISSING_CONFIGURATION_EXCEPTION =
      "Configuration parameter: selectSqlQuery cannot be null while using SQL reports!";

  SqlExecutorDto substitute(ReportRequestData fetchDataRequest) {
    Map<String, String> parameters = new HashMap<>();

    fetchDataRequest.getFrom()
        .ifPresent(from -> parameters.put(PARAMETER_FROM, toStringFormatIsoLocalDate(from)));
    fetchDataRequest.getTo()
        .ifPresent(to -> parameters.put(PARAMETER_TO, toStringFormatIsoLocalDate(to)));
    parameters.put(PARAMETER_ANALYSIS_ID, fetchDataRequest.getAnalysisId());
    parameters.put(PARAMETER_COUNTRIES, toSqlList(fetchDataRequest.getDataAccessPermissions()));

    String sqlSelectQuery =
        ofNullable(replace(fetchDataRequest.getSelectSqlQuery(), parameters))
            .orElseThrow(() -> new IllegalArgumentException(MISSING_CONFIGURATION_EXCEPTION));

    List<String> sqlTemplates =
        ofNullable(fetchDataRequest.getSqlTemplates())
            .orElse(Collections.emptyList())
            .stream()
            .map(s -> replace(s, parameters))
            .collect(toList());

    return SqlExecutorDto
        .builder()
        .prepareDataSqlStatements(sqlTemplates)
        .selectSqlStatement(sqlSelectQuery)
        .build();
  }

  private String toSqlList(Set<String> elements) {
    return elements.stream().collect(joining("', '", "'", "'"));
  }
}
