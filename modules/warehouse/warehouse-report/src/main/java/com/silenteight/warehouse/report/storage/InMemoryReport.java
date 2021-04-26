package com.silenteight.warehouse.report.storage;

import lombok.NonNull;
import lombok.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Value
public class InMemoryReport implements Report {

  @NonNull
  String reportName;

  @NonNull
  InputStream inputStream;

  public InMemoryReport(String reportName, byte[] bytes) {
    this.reportName = reportName;
    this.inputStream = new ByteArrayInputStream(bytes);
  }
}
