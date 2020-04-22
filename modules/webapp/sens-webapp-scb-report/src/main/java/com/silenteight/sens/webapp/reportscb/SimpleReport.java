package com.silenteight.sens.webapp.reportscb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.common.support.csv.SimpleLinesSupplier;

import java.util.List;

@RequiredArgsConstructor
class SimpleReport implements Report {

  @NonNull
  private final String fileName;
  @NonNull
  private final List<String> lines;

  @Override
  public String getReportFileName() {
    return fileName;
  }

  @Override
  public LinesSupplier getReportContent() {
    return new SimpleLinesSupplier(lines);
  }
}
