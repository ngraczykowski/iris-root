package com.silenteight.warehouse.report.storage;

import lombok.NonNull;
import lombok.Value;

import java.io.*;
import java.nio.file.Paths;

@Value
class FileSystemReport implements Report {

  @NonNull
  String reportName;

  @NonNull
  InputStream inputStream;

  FileSystemReport(String reportName, String reportPath) throws IOException {
    this.reportName = reportName;
    this.inputStream = java.nio.file.Files.newInputStream(Paths.get(reportPath));
  }
}
