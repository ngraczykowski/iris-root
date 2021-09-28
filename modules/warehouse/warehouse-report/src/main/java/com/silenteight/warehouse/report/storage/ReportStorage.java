package com.silenteight.warehouse.report.storage;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.reporting.Report;

import java.util.List;

public interface ReportStorage {

  void saveReport(Report report);

  void removeReport(String reportName);

  void removeReports(List<String> reportNames);

  FileDto getReport(String reportName);
}
