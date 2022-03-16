package com.silenteight.warehouse.report.storage;

import com.silenteight.warehouse.report.reporting.Report;

import java.util.Collection;

public interface ReportStorage {

  void saveReport(Report report);

  void removeReport(String reportName);

  void removeReports(Collection<String> reportNames);

  FileDto getReport(String reportName);
}
