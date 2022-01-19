package com.silenteight.warehouse.report.reporting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.query.streaming.DataProvider;
import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;
import com.silenteight.warehouse.report.storage.ReportStorage;
import com.silenteight.warehouse.report.storage.temporary.FileStorage;
import com.silenteight.warehouse.report.storage.temporary.TemporaryFileStorage;

import org.apache.commons.text.StringSubstitutor;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ReportGenerationService {

  private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final String PARAMETER_FROM = "from";
  private static final String PARAMETER_TO = "to";
  private static final String PARAMETER_ANALYSIS_ID = "analysisId";
  private static final String PARAMETER_COUNTRIES = "countries";
  private static final String MISSING_CONFIGURATION_EXCEPTION =
      "Configuration parameter: selectSqlQuery cannot be null while using SQL reports!";
  public static final String GROUP_NAME = "kibana-sso";

  @NonNull
  private final DataProvider dataProvider;
  @NonNull
  private final TemporaryFileStorage temporaryFileStorage;
  @NonNull
  private final ReportStorage reportStorage;
  @NonNull
  private final SqlExecutor sqlExecutor;
  @NonNull
  private final UserAwareTokenProvider userAwareTokenProvider;
  @NonNull
  private final CountryPermissionService countryPermissionService;

  public void generate(FetchDataRequest request) {
    if (request.isUseSqlReports()) {
      SqlExecutorDto dto = addSqlProperties(request);
      sqlExecutor.execute(
          dto, inputStream -> reportStorage.saveReport(toReport(request.getName(), inputStream)));
    } else {
      temporaryFileStorage.doOnTempFile(request.getName(), file -> doGenerate(request, file));
    }
  }

  private void doGenerate(FetchDataRequest request, FileStorage tmpFile) {
    dataProvider.fetchData(request, tmpFile.getConsumer());
    reportStorage.saveReport(toReport(request.getName(), tmpFile.getInputStream()));
  }

  private static Report toReport(String name, InputStream inputStream) {
    return InMemoryReportDto.builder()
        .reportName(name)
        .inputStream(inputStream)
        .build();
  }

  SqlExecutorDto addSqlProperties(FetchDataRequest fetchDataRequest) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put(PARAMETER_FROM, fetchDataRequest.getFrom().format(ISO_LOCAL_DATE));
    parameters.put(PARAMETER_TO, fetchDataRequest.getTo().format(ISO_LOCAL_DATE));
    parameters.put(PARAMETER_ANALYSIS_ID, fetchDataRequest.getName());
    parameters.put(PARAMETER_COUNTRIES, getSecurityParameters());

    String sqlSelectQuery = Optional
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

  String getSecurityParameters() {
    Set<String> roles = userAwareTokenProvider.getRolesForGroup(GROUP_NAME);
    log.info("Current roles: {}", String.join(", ", roles));
    return countryPermissionService.getCountries(roles).stream()
        .collect(Collectors.joining("', '", "'", "'"));
  }
}
