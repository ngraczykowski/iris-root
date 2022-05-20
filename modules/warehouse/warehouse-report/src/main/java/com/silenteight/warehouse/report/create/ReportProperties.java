package com.silenteight.warehouse.report.create;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Data
@ConstructorBinding
@RequiredArgsConstructor
public class ReportProperties {

  @NonNull
  private String name;
  @NonNull
  private String type;
  @NonNull
  private String description;
  private List<String> sqlTemplates = List.of();
  @NonNull
  private String selectSqlQuery;
}
