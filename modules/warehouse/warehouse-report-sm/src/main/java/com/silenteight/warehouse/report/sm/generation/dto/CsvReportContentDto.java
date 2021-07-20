package com.silenteight.warehouse.report.sm.generation.dto;

import lombok.Value;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;

@Value(staticConstructor = "of")
public class CsvReportContentDto {

  String labels;
  List<String> lines;

  public String getReport() {
    return concat(Stream.of(labels), lines.stream()).collect(joining());
  }
}
