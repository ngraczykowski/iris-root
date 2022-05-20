package com.silenteight.warehouse.report.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReportFileExtension {
  CSV("csv"),
  ZIP("zip");

  private final String fileExtension;
}
