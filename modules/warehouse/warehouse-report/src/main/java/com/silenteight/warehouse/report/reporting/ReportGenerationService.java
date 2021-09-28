package com.silenteight.warehouse.report.reporting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.streaming.DataProvider;
import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.report.storage.*;
import com.silenteight.warehouse.report.storage.temporary.FileStorage;
import com.silenteight.warehouse.report.storage.temporary.TemporaryFileStorage;

import java.io.InputStream;

@RequiredArgsConstructor
public class ReportGenerationService {

  @NonNull
  private final DataProvider dataProvider;
  @NonNull
  private final TemporaryFileStorage temporaryFileStorage;
  @NonNull
  private final ReportStorage reportStorage;

  public void generate(FetchDataRequest request) {
    temporaryFileStorage.doOnTempFile(request.getName(), file -> doGenerate(request, file));
  }

  private void doGenerate(FetchDataRequest request, FileStorage tmpFile) {
    dataProvider.fetchData(request, tmpFile.getConsumer());
    reportStorage.saveReport(toReport(request.getName(), tmpFile.getInputStream()));
  }

  private static Report toReport(String name, InputStream inputStream) {
    return InMemoryReportDto.builder()
        .reportName(name)
        .inputStream(inputStream)
        .build();
  }
}
