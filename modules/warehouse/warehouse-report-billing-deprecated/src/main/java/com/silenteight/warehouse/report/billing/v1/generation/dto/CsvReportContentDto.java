package com.silenteight.warehouse.report.billing.v1.generation.dto;

import lombok.Value;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

@Value
public class CsvReportContentDto {

  String labels;
  List<String> lines;
  String checksum;

  public String getReport() {
    return of(of(labels), lines.stream(), of("\n"), of(checksum))
        .flatMap(Function.identity())
        .collect(joining());
  }
}
