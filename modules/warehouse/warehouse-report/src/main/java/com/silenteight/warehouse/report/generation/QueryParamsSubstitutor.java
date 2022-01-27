package com.silenteight.warehouse.report.generation;

import lombok.experimental.UtilityClass;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

import org.apache.commons.text.StringSubstitutor;

import java.util.*;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.common.time.Timestamps.toStringFormatIsoLocalDate;

@UtilityClass
class QueryParamsSubstitutor {

  private static final String PARAMETER_FROM = "from";
  private static final String PARAMETER_TO = "to";
  private static final String PARAMETER_ANALYSIS_ID = "analysisId";
  private static final String MISSING_CONFIGURATION_EXCEPTION =
      "Configuration parameter: selectSqlQuery cannot be null while using SQL reports!";

  SqlExecutorDto substitute(ReportRequestData fetchDataRequest) {
    Map<String, String> parameters = new HashMap<>();

    fetchDataRequest
        .getFrom()
        .ifPresent(from -> parameters.put(PARAMETER_FROM, toStringFormatIsoLocalDate(from)));
    fetchDataRequest
        .getTo()
        .ifPresent(to -> parameters.put(PARAMETER_TO, toStringFormatIsoLocalDate(to)));
    parameters.put(PARAMETER_ANALYSIS_ID, fetchDataRequest.getAnalysisId());

    String sqlSelectQuery =
        Optional
            .ofNullable(StringSubstitutor.replace(fetchDataRequest.getSelectSqlQuery(), parameters))
            .orElseThrow(() -> new IllegalArgumentException(MISSING_CONFIGURATION_EXCEPTION));

    List<String> sqlTemplates =
        Optional.ofNullable(fetchDataRequest.getSqlTemplates())
            .orElse(Collections.emptyList())
            .stream()
            .map(s -> StringSubstitutor.replace(s, parameters))
            .collect(Collectors.toList());

    return SqlExecutorDto
        .builder()
        .prepareDataSqlStatements(sqlTemplates)
        .selectSqlStatement(sqlSelectQuery)
        .build();
  }
}
