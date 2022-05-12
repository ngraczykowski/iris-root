package com.silenteight.warehouse.report.sql.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SqlExecutorDto {

  @NonNull
  List<String> prepareDataSqlStatements;
  @NonNull
  String selectSqlStatement;
}
